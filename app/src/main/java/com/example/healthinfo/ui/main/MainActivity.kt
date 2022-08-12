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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthinfo.R
import com.example.healthinfo.data.local.entity.AccessTokenEntity
import com.example.healthinfo.data.remote.dto.QuestionsTitleDto
import com.example.healthinfo.databinding.ActivityMainBinding
import com.example.healthinfo.ui.answer_list.AnswerListActivity
import com.example.healthinfo.ui.ask_question.AskQuestion
import com.example.healthinfo.ui.login.LoginActivity
import com.example.healthinfo.ui.main.adapter.QuestionTitleAdapter
import com.example.healthinfo.ui.signup.SignUpActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), QuestionTitleAdapter.Interaction {

    private lateinit var binding: ActivityMainBinding
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
                        questionTitleAdapter.submitList(it.QuestionTitleItems)
                    }
                }
            }
        }
        binding.fab.setOnClickListener { view ->
            lifecycleScope.launchWhenStarted {
                mainActivityViewModel.tokenState.collectLatest {
                    when(it.accessTokenData) {
                        emptyList<AccessTokenEntity>() -> {
                            val intent = Intent(Intent(this@MainActivity, SignUpActivity::class.java))
                            startActivity(intent)
                            finish()
                        }
                        else -> {
                            val intent = Intent(Intent(this@MainActivity, AskQuestion::class.java))
                            startActivity(intent)
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
                when(it.isLoading) {
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
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                // task HERE
                //on submit send entire query
                println(query)
                return false
            }

        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.setting_menu -> {
                println("setting")
                true
            }
            R.id.profile_menu -> {

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
                                            true-> binding.progressBar.visibility = View.VISIBLE
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

    override fun onItemSelected(position: Int, item: QuestionsTitleDto) {

        val intent = Intent(Intent(this, AnswerListActivity::class.java))
        intent.putExtra("question_title",item.question_title)
        startActivity(intent)
        finish()
    }

//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        return navController.navigateUp(appBarConfiguration)
//                || super.onSupportNavigateUp()
//    }
}