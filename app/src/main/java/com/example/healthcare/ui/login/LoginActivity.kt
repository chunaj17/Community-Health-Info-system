package com.example.healthcare.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.healthcare.databinding.ActivityLoginBinding
import com.example.healthcare.ui.signup.SignUpActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(this.layoutInflater)
        binding.signup.setOnClickListener {
            val intent = Intent(Intent(this, SignUpActivity::class.java))
            startActivity(intent)
            finish()
        }
        val emailEditText = binding.email.editText
        val passwordEditText = binding.password.editText
        val loginButton = binding.loginBtn
        val loadingProgressBar = binding.progressBar
        loginButton.setOnClickListener {
            when {
                emailEditText!!.text.isEmpty() -> emailEditText.error = "Enter Email"
                passwordEditText!!.text.isEmpty() -> passwordEditText.error = "Enter password"
                else -> {
                    val email = emailEditText.text.toString().trim { it <= ' ' }
                    val password = passwordEditText.text.toString().trim { it <= ' ' }
                    loadingProgressBar.visibility = ProgressBar.VISIBLE
                    viewModel.authLogin(email, password)
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.loginState.collectLatest {
                when (it.isLoading) {
                    true -> binding.progressBar.visibility = View.VISIBLE
                    false ->  binding.progressBar.visibility = View.GONE

                }
            }
        }

        setContentView(binding.root)
    }

}