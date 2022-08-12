package com.example.healthinfo.ui.more_profile.user

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.healthinfo.databinding.ActivityMoreUserSignUpBinding
import com.example.healthinfo.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MoreUserSignUp : AppCompatActivity() {
    lateinit var binding:ActivityMoreUserSignUpBinding
    private val viewModel:MoreUserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoreUserSignUpBinding.inflate(this.layoutInflater)
        val emailString = intent.getStringExtra("email")
        val firstNameText = binding.firstName.editText
        val lastNameText = binding.lastName.editText
        val ageText = binding.age.editText
        val homeAddressText = binding.homeAddress.editText

        binding.finishBtn.setOnClickListener {
            val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
            when {
                firstNameText!!.text.isEmpty() -> {
                    firstNameText.error = "enter first name"
                }
                lastNameText!!.text.isEmpty() -> {
                    lastNameText.error = "enter last name"
                }
                ageText!!.text.isEmpty() -> {
                    ageText.error =
                        "enter age"
                }
                homeAddressText!!.text.isEmpty() -> {
                    homeAddressText.error =
                        "enter home address"
                }

                else -> {
                    val firstName = firstNameText.text.toString()
                    val lastName = lastNameText.text.toString()
                    val age = ageText.text.toString()
                    val homeAddress = homeAddressText.text.toString()
                    viewModel.moreUserRegistration(firstName,lastName,age,null,homeAddress,emailString!!)
                    lifecycleScope.launchWhenStarted {
                        viewModel.moreUserState.collectLatest {
                            when(it.isLoading) {
                                true -> binding.progressBar.visibility = View.VISIBLE
                                false -> {
                                    binding.progressBar.visibility = View.GONE
                                    when (it.data) {
                                        null -> println(null)
                                        else -> {
                                            withContext(Dispatchers.Main) {
                                                val intent = Intent(Intent(this@MoreUserSignUp, MainActivity::class.java))
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
        }
        setContentView(binding.root)
    }
}