package com.example.healthinfo.ui.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.afollestad.materialdialogs.MaterialDialog
import com.example.healthinfo.databinding.ActivitySignUpBinding
import com.example.healthinfo.databinding.DoctorOrPatientCustomDialogueBinding
import com.example.healthinfo.ui.login.LoginActivity
import com.example.healthinfo.ui.more_profile.doctor.MoreDoctorSignUp
import com.example.healthinfo.ui.more_profile.user.MoreUserSignUp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding
    lateinit var dialogueBinding: DoctorOrPatientCustomDialogueBinding
    private val viewModel: SignupViewModel by viewModels()
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
                emailEditText!!.text.isEmpty() -> {
                    emailEditText.error = "Enter Email"
                }
                passwordEditText!!.text.isEmpty() -> {
                    passwordEditText.error = "Enter password"
                }
                confirmPasswordEditText!!.text.isEmpty() -> {
                    confirmPasswordEditText.error =
                        "Confirm Password"
                }
                passwordEditText.text.toString() != confirmPasswordEditText.text.toString() -> {
                    confirmPasswordEditText.error =
                        "password don't match"
                }
                else -> {
                    val email = emailEditText.text.toString().trim { it <= ' ' }
                    val password = passwordEditText.text.toString().trim { it <= ' ' }
                    loadingProgressBar.visibility = ProgressBar.VISIBLE
                    viewModel.signup(email, password)
                    dialogueBinding =
                        DoctorOrPatientCustomDialogueBinding.inflate(this.layoutInflater)
                    val signUpDialog = MaterialDialog(this)
                    signUpDialog.setContentView(dialogueBinding.root)
                    signUpDialog.noAutoDismiss()
                    signUpDialog.show()
                    dialogueBinding.doctorBtn.setOnClickListener {
                        val intent = Intent(Intent(this, MoreDoctorSignUp::class.java))
                        intent.putExtra("email", email)
                        signUpDialog.dismiss()
                        startActivity(intent)
                        finish()
                    }
                    dialogueBinding.patientBtn.setOnClickListener {
                        val intent = Intent(Intent(this, MoreUserSignUp::class.java))
                        intent.putExtra("email", email)
                        signUpDialog.dismiss()
                        startActivity(intent)
                        finish()
                    }

                }
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.signupState.collectLatest {
                when (it.isLoading) {
                    true -> binding.progressBar.visibility = View.VISIBLE
                    false -> binding.progressBar.visibility = View.GONE
                }
            }
        }
        setContentView(binding.root)
    }
}