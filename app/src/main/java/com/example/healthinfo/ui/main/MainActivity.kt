package com.example.healthinfo.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.example.healthinfo.R
import com.example.healthinfo.data.local.entity.AccessTokenEntity
import com.example.healthinfo.data.remote.dto.question_title_dto.QuestionTitleDataDto
import com.example.healthinfo.data.remote.dto.question_title_dto.QuestionsTitleDto
import com.example.healthinfo.databinding.ActivityMainBinding
import com.example.healthinfo.databinding.AskQuestionDialogueBinding
import com.example.healthinfo.ui.answer_list.AnswerListActivity
import com.example.healthinfo.ui.ask_question.AskQuestionViewModel
import com.example.healthinfo.ui.login.LoginActivity
import com.example.healthinfo.ui.main.adapter.QuestionTitleAdapter
import com.example.healthinfo.ui.profile.ProfileActivity
import com.example.healthinfo.ui.signup.SignUpActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), QuestionTitleAdapter.Interaction {


    private lateinit var binding: ActivityMainBinding
    private lateinit var arrayList: ArrayList<QuestionTitleDataDto>
    private val askQuestionViewModel: AskQuestionViewModel by viewModels()
    private lateinit var dialogueBinding: AskQuestionDialogueBinding
    private val mainActivityViewModel: MainActivityViewModel by viewModels()
    private lateinit var questionTitleAdapter: QuestionTitleAdapter
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        mainActivityViewModel.getQuestionTitle()
        binding.questionRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            questionTitleAdapter = QuestionTitleAdapter(this@MainActivity)
            adapter = questionTitleAdapter
        }
        lifecycleScope.launchWhenStarted {
            mainActivityViewModel.questionTitleState.collectLatest {
                when (it.isLoading) {
                    true -> binding.progressBar.visibility = View.VISIBLE
                    false -> {
                        binding.progressBar.visibility = View.GONE

                        when (it.QuestionTitleItems) {
                            null -> binding.progressBar.visibility = View.VISIBLE
                            else -> {
                                binding.progressBar.visibility = View.GONE
                                questionTitleAdapter.submitList(it.QuestionTitleItems.data)
                            }
                        }
                    }
                }
            }
        }
        binding.fab.setOnClickListener { view ->
            lifecycleScope.launchWhenStarted {
                mainActivityViewModel.tokenState.collectLatest { TokenState ->
                    when (TokenState.accessTokenData) {
                        emptyList<AccessTokenEntity>() -> {
                            val intent =
                                Intent(Intent(this@MainActivity, SignUpActivity::class.java))
                            startActivity(intent)
                            finish()
                        }
                        else -> {
                            dialogueBinding =
                                AskQuestionDialogueBinding.inflate(this@MainActivity.layoutInflater)
                            val questionTitle = dialogueBinding.editTextTitle.editText
                            val questionDetail = dialogueBinding.detailQuestionEditText.editText
                            questionTitle!!.addTextChangedListener { editable ->
                                editable?.let {
                                    if (it.isEmpty()) {
                                        dialogueBinding.dialogFab.visibility = View.GONE
                                    } else {
                                        dialogueBinding.dialogFab.visibility = View.VISIBLE
                                    }
                                }
                            }
                            val askQuestionDialog = MaterialDialog(this@MainActivity)
                            askQuestionDialog.setContentView(dialogueBinding.root)
                            askQuestionDialog.show()
                            TokenState.accessTokenData?.let { accessTokenEntity ->
                                val email = accessTokenEntity[0].email!!
                                dialogueBinding.dialogFab.setOnClickListener { view ->
                                    dialogueBinding.progressBar.visibility = View.VISIBLE
                                    when {
                                        questionTitle.text.isEmpty() -> questionTitle.error =
                                            "add question title"
                                        questionDetail!!.text.isEmpty() -> questionDetail.error =
                                            "add your question detail"
                                        else -> {
                                            val title = questionTitle.text.toString()
                                            val detailText = questionDetail.text.toString()
                                            askQuestionViewModel.askQuestion(
                                                email,
                                                null,
                                                detailText,
                                                title
                                            )
                                            Toast.makeText(
                                                this@MainActivity,
                                                "Question added",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            mainActivityViewModel.getQuestionTitle()
                                            askQuestionDialog.dismiss()
                                            recreate()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchItem = menu.findItem(R.id.search)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Search for your question?"
        mainActivityViewModel.checkAccessToken()
        lifecycleScope.launchWhenStarted {
            mainActivityViewModel.tokenState.collectLatest {
                when (it.isLoading) {
                    true -> binding.progressBar.visibility = View.VISIBLE
                    false -> {
                        binding.progressBar.visibility = View.GONE
                        when (it.accessTokenData) {
                            emptyList<List<AccessTokenEntity>>() -> {
                                menu.findItem(R.id.profile_menu).isVisible = false
                                menu.findItem(R.id.login_menu).isVisible = true
                                menu.findItem(R.id.logout_menu).isVisible = false
                            }
                            else -> {
                                menu.findItem(R.id.profile_menu).isVisible = true
                                menu.findItem(R.id.logout_menu).isVisible = true
                                menu.findItem(R.id.login_menu).isVisible = false
                            }
                        }
                    }
                }
            }
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                lifecycleScope.launchWhenStarted {
                    mainActivityViewModel.questionTitleState.collectLatest { QuestionTitleState ->
                        when (QuestionTitleState.isLoading) {
                            true -> binding.progressBar.visibility = View.VISIBLE
                            false -> {
                                binding.progressBar.visibility = View.GONE
                                when (QuestionTitleState.QuestionTitleItems) {
                                    null -> binding.progressBar.visibility = View.VISIBLE
                                    else -> {
                                        binding.progressBar.visibility = View.GONE
                                        if (newText.isNotEmpty()) {
                                            arrayList = arrayListOf(
                                                QuestionTitleDataDto(null, null, null)
                                            )
                                            arrayList.clear()
                                            QuestionTitleState.QuestionTitleItems.data.forEachIndexed { index, questionTitleDataDto ->
                                                if (questionTitleDataDto.question_title.toString()
                                                        .contains(newText.trim())
                                                ) {
                                                    arrayList.add(questionTitleDataDto)
                                                }
                                            }
                                            questionTitleAdapter.submitList(arrayList)
                                            when(questionTitleAdapter.itemCount) {
                                                0 -> {
                                                    binding.emptyAnimation.visibility = View.VISIBLE
                                                    binding.questionRecyclerView.visibility = View.GONE
                                                }
                                                else -> {
                                                    binding.emptyAnimation.visibility = View.GONE
                                                    binding.questionRecyclerView.visibility = View.VISIBLE
                                                }
                                            }
                                        } else {
                                            binding.emptyAnimation.visibility = View.GONE
                                            binding.questionRecyclerView.visibility = View.VISIBLE
                                            questionTitleAdapter.submitList(QuestionTitleState.QuestionTitleItems.data)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {

                return false
            }

        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.setting_menu -> {
                println("setting")
                true
            }
            R.id.profile_menu -> {
                val intent = Intent(Intent(this, ProfileActivity::class.java))
                startActivity(intent)
                true
            }
            R.id.login_menu -> {
                val intent = Intent(Intent(this, LoginActivity::class.java))
                startActivity(intent)
                finish()
                true
            }
            R.id.logout_menu -> {
                lifecycleScope.launchWhenStarted {
                    mainActivityViewModel.tokenState.collectLatest { tokenState ->
                        when (tokenState.isLoading) {
                            true -> binding.progressBar.visibility = View.VISIBLE
                            false -> {
                                tokenState.accessTokenData?.let {
                                    val email = it[0].email
                                    mainActivityViewModel.logOut(email!!)
                                    mainActivityViewModel.logoutState.collectLatest {
                                        when (it.isLoading) {
                                            false -> {
                                                binding.progressBar.visibility = View.GONE
                                                Toast.makeText(
                                                    this@MainActivity,
                                                    "user Logged out",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                recreate()
                                            }
                                            true -> binding.progressBar.visibility = View.VISIBLE
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                true
            }
            else -> false
        }
    }

    override fun onItemSelected(position: Int, item: QuestionTitleDataDto) {
        mainActivityViewModel.checkAccessToken()

        lifecycleScope.launchWhenStarted {
            mainActivityViewModel.tokenState.collectLatest { tokenState ->
                when (tokenState.isLoading) {
                    true -> binding.progressBar.visibility = View.VISIBLE
                    false -> {
                        when (tokenState.accessTokenData) {
                            emptyList<AccessTokenEntity>() -> {
                                binding.progressBar.visibility = View.GONE
                                mainActivityViewModel.addView(item.question_title!!)
                                val intent = Intent(
                                    Intent(
                                        this@MainActivity,
                                        AnswerListActivity::class.java
                                    )
                                )
                                intent.putExtra("question_title", item.question_title)
                                startActivity(intent)
                                finish()
                            }
                            null -> binding.progressBar.visibility = View.VISIBLE
                            else -> {
                                binding.progressBar.visibility = View.GONE
                                mainActivityViewModel.addView(item.question_title!!)
                                tokenState.accessTokenData.let {
                                    val email = it[0].email
                                    val intent = Intent(
                                        Intent(
                                            this@MainActivity,
                                            AnswerListActivity::class.java
                                        )
                                    )
                                    intent.putExtra("question_title", item.question_title)
                                    intent.putExtra("email", email)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        return navController.navigateUp(appBarConfiguration)
//                || super.onSupportNavigateUp()
//    }
}