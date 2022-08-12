package com.example.healthinfo.ui.more_profile.doctor

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.healthinfo.databinding.ActivityMoreDoctorSignUpBinding
import com.example.healthinfo.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
@AndroidEntryPoint
class MoreDoctorSignUp : AppCompatActivity() {
    lateinit var binding: ActivityMoreDoctorSignUpBinding
    private val viewModel:MoreDoctorViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoreDoctorSignUpBinding.inflate(this.layoutInflater)
        val emailString = intent.getStringExtra("email")
        val firstNameText = binding.firstName.editText
        val lastNameText = binding.lastName.editText
        val ageText = binding.age.editText
        val experienceText = binding.experience.editText
        val specializationText = binding.specialization.editText
        val workLocationText = binding.workLocation.editText
        val homeAddressText = binding.homeAddress.editText
        val loadingProgressBar = binding.progressBar

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
                experienceText!!.text.isEmpty() -> {
                    experienceText.error = "Enter experience "
                }
                specializationText!!.text.isEmpty() -> {
                    specializationText.error =
                        "enter specialization"
                }
                workLocationText!!.text.isEmpty() -> {
                    workLocationText.error = "enter work location "
                }
                homeAddressText!!.text.isEmpty() -> {
                    homeAddressText.error =
                        "enter home address"
                }

                else -> {
                    val firstName = firstNameText.text.toString()
                    val lastName = lastNameText.text.toString()
                    val age = ageText.text.toString()
                    val experience= experienceText.text.toString()
                    val specialization = specializationText.text.toString()
                    val workLocation = workLocationText.text.toString()
                    val homeAddress = homeAddressText.text.toString()
                    viewModel.moreDoctorRegistration(age,firstName,lastName, null, homeAddress,emailString!!,specialization,experience,workLocation)
                    lifecycleScope.launchWhenStarted {
                        viewModel.moreDoctorState.collectLatest {
                            when(it.isLoading) {
                                true -> binding.progressBar.visibility = View.VISIBLE
                                false -> {
                                    binding.progressBar.visibility = View.GONE
                                    when (it.data) {
                                        null -> println(null)
                                        else -> println(it.data)
                                    }
                                }
                            }
                        }
                    }
                    val intent = Intent(Intent(this, MainActivity::class.java))
                    startActivity(intent)
                    finish()
                }
            }
        }
        setContentView(binding.root)
    }
}