package com.example.healthinfo.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.healthinfo.databinding.ActivityLoginBinding
import com.example.healthinfo.ui.main.MainActivity
import com.example.healthinfo.ui.signup.SignUpActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
                    viewModel.authLogin(email, password)
                    lifecycleScope.launchWhenStarted{
                        viewModel.loginState.collectLatest {
                    when (it.isLoading) {
                        true -> binding.progressBar.visibility = View.VISIBLE
                        false -> {
                            when(it.AccessAndRefreshTokenData) {
                                null -> binding.progressBar.visibility = View.VISIBLE
                                else -> {
                                    binding.progressBar.visibility = View.GONE
                                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
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
        }

        setContentView(binding.root)
    }

}