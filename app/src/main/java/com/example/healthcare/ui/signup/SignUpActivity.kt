package com.example.healthcare.ui.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.healthcare.databinding.ActivitySignUpBinding
import com.example.healthcare.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {
    lateinit var binding:ActivitySignUpBinding
    private val viewModel:SignupViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(this.layoutInflater)
        binding.login.setOnClickListener {
            val intent = Intent(Intent(this, LoginActivity::class.java))
            startActivity(intent)
            finish()
        }
        val emailEditText = binding.email.editText
        val passwordEditText = binding.password.editText
        val nextButton = binding.nextBtn
        val loadingProgressBar = binding.progressBar
        val confirmPasswordEditText = binding.confirmButton.editText
        nextButton.setOnClickListener {
            when {
                emailEditText!!.text.isEmpty() -> emailEditText.error = "Enter Email"
                passwordEditText!!.text.isEmpty() -> passwordEditText.error = "Enter password"
                confirmPasswordEditText!!.text.isEmpty() -> confirmPasswordEditText.error =
                    "Confirm Password"
                passwordEditText.text.toString() != confirmPasswordEditText.text.toString() -> confirmPasswordEditText.error =
                    "password don't match"
                else -> {
                    val email = emailEditText.text.toString().trim { it <= ' ' }
                    val password = passwordEditText.text.toString().trim { it <= ' ' }
                    loadingProgressBar.visibility = ProgressBar.VISIBLE
                    viewModel.signup(email, password)
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.signupState.collectLatest {
                println(it.data)
                when (it.isLoading) {
                    true -> binding.progressBar.visibility = View.VISIBLE
                    false ->  binding.progressBar.visibility = View.GONE
                }
            }
        }
        setContentView(binding.root)
    }
}