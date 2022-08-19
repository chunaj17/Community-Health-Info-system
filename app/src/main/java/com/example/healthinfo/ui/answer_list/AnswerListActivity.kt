package com.example.healthinfo.ui.answer_list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthinfo.R
import com.example.healthinfo.data.local.entity.AccessTokenEntity
import com.example.healthinfo.data.remote.dto.answer_dto.DataDto
import com.example.healthinfo.databinding.ActivityAnswerListBinding
import com.example.healthinfo.ui.answer_list.adapter.AnswerListAdapter
import com.example.healthinfo.ui.main.MainActivity
import com.example.healthinfo.ui.main.MainActivityViewModel
import com.example.healthinfo.ui.main.adapter.QuestionTitleAdapter
import com.example.healthinfo.ui.profile.ProfileActivity
import com.example.healthinfo.ui.signup.SignUpActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class AnswerListActivity : AppCompatActivity(), AnswerListAdapter.Interaction {
    private val mainActivityViewModel: MainActivityViewModel by viewModels()
    lateinit var binding: ActivityAnswerListBinding
    val viewModel: AnswerListViewModel by viewModels()
    lateinit var answerListAdapter: AnswerListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnswerListBinding.inflate(this.layoutInflater)
        val questionTitle = intent.getStringExtra("question_title")
        mainActivityViewModel.checkAccessToken()
        viewModel.getQuestionDetail(questionTitle!!)
        viewModel.getAnswerList(questionTitle)
        binding.answerListRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@AnswerListActivity)
            answerListAdapter = AnswerListAdapter(this@AnswerListActivity)
            adapter = answerListAdapter
        }
        lifecycleScope.launchWhenStarted {
            viewModel.answerListState.collectLatest {
                when (it.isLoading) {
                    true -> binding.progressBar.visibility = View.VISIBLE
                    false -> {
                        when (it.data) {
                            null -> {
                                binding.progressBar.visibility = View.VISIBLE
                                binding.emptyAnimation.visibility = View.VISIBLE
                                binding.answerListRecyclerView.visibility = View.GONE
                            }
                            else -> {
                                binding.progressBar.visibility = View.GONE
                                binding.emptyAnimation.visibility = View.GONE
                                binding.answerListRecyclerView.visibility = View.VISIBLE
                                answerListAdapter.submitList(it.data.data)
                            }
                        }
                    }
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            mainActivityViewModel.tokenState.collectLatest { TokenState ->
                when (TokenState.accessTokenData) {
                    emptyList<AccessTokenEntity>() -> {
                        binding.answerAndSendReplayParentCard.visibility = View.GONE
                        binding.fab.visibility = View.VISIBLE
                        binding.fab.setOnClickListener {
                            Toast.makeText(
                                this@AnswerListActivity,
                                "Signup first to add your answers",
                                Toast.LENGTH_LONG
                            ).show()
                            val intent =
                                Intent(Intent(this@AnswerListActivity, SignUpActivity::class.java))
                            startActivity(intent)
                            finish()

                        }
                    }
                    else -> {
                        binding.answerAndSendReplayParentCard.visibility = View.VISIBLE
                        binding.fab.visibility = View.GONE
                        binding.sendBtn.setOnClickListener {
                            val email = TokenState.accessTokenData?.let {
                                it[0].email
                            }
                            val answerText = binding.answerEditText.text.toString()
                            viewModel.replayQuestion(email!!, questionTitle, answerText, null)
                            viewModel.getAnswerList(questionTitle)
                            lifecycleScope.launchWhenStarted {
                                viewModel.replayQuestionState.collectLatest {
                                    when (it.isLoading) {
                                        true -> binding.progressBar.visibility = View.VISIBLE
                                        false -> {
                                            when (it.data) {

                                                null -> binding.progressBar.visibility =
                                                    View.VISIBLE
                                                else -> {
                                                    binding.progressBar.visibility = View.GONE
                                                    viewModel.getAnswerList(questionTitle)
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
        }
        lifecycleScope.launchWhenStarted {
            viewModel.questionDetailState.collectLatest {
                when (it.isLoading) {
                    true -> binding.progressBar.visibility = View.VISIBLE
                    false -> {
                        when (it.data) {
                            null -> binding.progressBar.visibility = View.VISIBLE
                            else -> {
                                binding.progressBar.visibility = View.GONE
                                binding.questionTitle.text = questionTitle
                                binding.questionBody.text = it.data.data[0].question_text
                            }
                        }
                    }
                }
            }
        }
        binding.backBtn.setOnClickListener {
            val intent = Intent(Intent(this, MainActivity::class.java))
            startActivity(intent)
            finish()
        }
        binding.profileBtn.setOnClickListener {
            val intent = Intent(Intent(this, ProfileActivity::class.java))
            startActivity(intent)
            finish()
        }
        setContentView(binding.root)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onItemSelected(position: Int, item: DataDto) {

    }

    override fun addVote(item: DataDto) {
        lifecycleScope.launchWhenStarted {
            mainActivityViewModel.tokenState.collectLatest { TokenState ->
                when (TokenState.accessTokenData) {
                    emptyList<AccessTokenEntity>() -> {
                        Toast.makeText(
                            this@AnswerListActivity,
                            "Signup first so you vote for answers",
                            Toast.LENGTH_LONG
                        ).show()
                        val intent =
                            Intent(Intent(this@AnswerListActivity, SignUpActivity::class.java))
                        startActivity(intent)
                        finish()
                    }
                    else -> {
                        val email = intent.getStringExtra("email")
                        val questionTitle = intent.getStringExtra("question_title")
                        viewModel.addVote(email!!, item.answer_text)
                        lifecycleScope.launchWhenStarted {
                            viewModel.addVoteState.collectLatest { addVoteState ->
                                when (addVoteState.isLoading) {
                                    true -> binding.progressBar.visibility = View.VISIBLE
                                    false -> {

                                        when (addVoteState.data) {
                                            null -> binding.progressBar.visibility =
                                                View.VISIBLE
                                            else -> {
                                                binding.progressBar.visibility = View.GONE
                                                viewModel.getAnswerList(questionTitle!!)
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
    }


    override fun removeVote(item: DataDto) {
        lifecycleScope.launchWhenStarted {
            mainActivityViewModel.tokenState.collectLatest { TokenState ->
                when (TokenState.accessTokenData) {
                    emptyList<AccessTokenEntity>() -> {
                        Toast.makeText(
                            this@AnswerListActivity,
                            "Signup first so you vote for answers",
                            Toast.LENGTH_LONG
                        ).show()
                        val intent =
                            Intent(Intent(this@AnswerListActivity, SignUpActivity::class.java))
                        startActivity(intent)
                        finish()
                    }
                    else -> {
                        val email = intent.getStringExtra("email")
                        val questionTitle = intent.getStringExtra("question_title")
                        viewModel.removeVote(email!!, item.answer_text)
                        lifecycleScope.launchWhenStarted {
                            viewModel.removeVoteState.collectLatest { removeVoteState ->
                                when (removeVoteState.isLoading) {
                                    true -> binding.progressBar.visibility = View.VISIBLE
                                    false -> {

                                        when (removeVoteState.data) {
                                            null -> binding.progressBar.visibility =
                                                View.VISIBLE
                                            else -> {
                                                binding.progressBar.visibility = View.GONE
                                                viewModel.getAnswerList(questionTitle!!)
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
    }
}