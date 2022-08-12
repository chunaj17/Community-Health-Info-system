package com.example.healthinfo.ui.answer_list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthinfo.data.remote.dto.answer_dto.DataDto
import com.example.healthinfo.databinding.ActivityAnswerListBinding
import com.example.healthinfo.ui.answer_list.adapter.AnswerListAdapter
import com.example.healthinfo.ui.main.MainActivity
import com.example.healthinfo.ui.main.adapter.QuestionTitleAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class AnswerListActivity : AppCompatActivity(), AnswerListAdapter.Interaction {
    lateinit var binding: ActivityAnswerListBinding
    val viewModel: AnswerListViewModel by viewModels()
    lateinit var answerListAdapter: AnswerListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnswerListBinding.inflate(this.layoutInflater)
        val questionTitle = intent.getStringExtra("question_title")
        println(questionTitle)
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
                            null -> binding.progressBar.visibility = View.VISIBLE
                            else -> {
                                binding.progressBar.visibility = View.GONE
                                answerListAdapter.submitList(it.data.data)
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
        setContentView(binding.root)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onItemSelected(position: Int, item: DataDto) {

    }
}