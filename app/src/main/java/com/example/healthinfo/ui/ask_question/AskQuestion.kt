package com.example.healthinfo.ui.ask_question

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.example.healthinfo.data.local.entity.AccessTokenEntity
import com.example.healthinfo.databinding.ActivityAskQuestionBinding
import com.example.healthinfo.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class AskQuestion : AppCompatActivity() {
    private val viewModel: AskQuestionViewModel by viewModels()
    private lateinit var binding: ActivityAskQuestionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAskQuestionBinding.inflate(this.layoutInflater)
        val questionTitle = binding.editTextTitle.editText
        val questionDetail = binding.detailQuestionEditText.editText
        questionTitle!!.addTextChangedListener {
            binding.fab.visibility = View.VISIBLE
        }
        viewModel.checkAccessToken()
        lifecycleScope.launchWhenStarted {
            viewModel.tokenState.collectLatest {
                when (it.isLoading) {
                    true -> binding.progressBar.visibility = View.VISIBLE
                    false -> {
                        when (it.accessTokenData) {
                            emptyList<AccessTokenEntity>() -> binding.progressBar.visibility =
                                View.VISIBLE
                            else -> {
                                binding.progressBar.visibility = View.GONE
                                it.accessTokenData?.let { accessTokenEntity ->
                                    val email = accessTokenEntity[0].email!!
                                    binding.fab.setOnClickListener { view ->
                                        when {
                                            questionTitle.text.isEmpty() -> questionTitle.error =
                                                "add question title"
                                            questionDetail!!.text.isEmpty() -> questionDetail.error =
                                                "add your question detail"
                                            else -> {
                                                val title = questionTitle.text.toString()
                                                val detailText = questionDetail.text.toString()
                                                viewModel.askQuestion(email,null, detailText, title)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
            viewModel.askQuestionState.collectLatest {
                println(it.data)
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
}