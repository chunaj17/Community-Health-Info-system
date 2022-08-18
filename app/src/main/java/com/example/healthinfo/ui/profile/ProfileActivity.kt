package com.example.healthinfo.ui.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.healthinfo.data.local.entity.AccessTokenEntity
import com.example.healthinfo.databinding.ActivityProfileBinding
import com.example.healthinfo.ui.main.MainActivity
import com.example.healthinfo.ui.main.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {
    lateinit var binding:ActivityProfileBinding
    val viewModel : ProfileActivityViewModel by viewModels()
    val mainActivityViewModel:MainActivityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(this.layoutInflater)
        mainActivityViewModel.checkAccessToken()
        lifecycleScope.launchWhenStarted {
            mainActivityViewModel.tokenState.collectLatest {TokenState ->
                when(TokenState.isLoading){
                    true -> binding.progressBar.visibility = View.VISIBLE
                    false -> {
                        when(TokenState.accessTokenData) {
                            emptyList<AccessTokenEntity>() -> binding.progressBar.visibility = View.VISIBLE
                            null -> binding.progressBar.visibility = View.VISIBLE
                            else -> {
                                val email = TokenState.accessTokenData[0].email.toString()
                                viewModel.identifyUser(email)
                                viewModel.userState.collectLatest { userState ->
                                    when (userState.isLoading) {
                                        true -> binding.progressBar.visibility = View.VISIBLE
                                        false -> {
                                           when(userState.data) {
                                               null -> binding.progressBar.visibility = View.VISIBLE
                                               else -> {
                                                   when(userState.data.msg) {
                                                       "doctor" -> {
                                                           viewModel.getDoctorProfile(email)
                                                           viewModel.doctorProfileState.collectLatest { doctorProfileState ->
                                                               when(doctorProfileState.isLoading) {
                                                                   true -> binding.progressBar.visibility = View.VISIBLE
                                                                   false -> {
                                                                       when(doctorProfileState.data) {
                                                                           null -> binding.progressBar.visibility = View.VISIBLE
                                                                           else -> {
                                                                               binding.progressBar.visibility = View.GONE
                                                                               val firstName = doctorProfileState.data.first_name
                                                                               val lastName = doctorProfileState.data.last_name
                                                                               val specialization = doctorProfileState.data.specialization
                                                                               val experience = doctorProfileState.data.experience.toString()
                                                                               val homeAddress = doctorProfileState.data.address
                                                                               val age = doctorProfileState.data.age.toString()
                                                                               binding.doctorFirstName.text = firstName
                                                                               binding.doctorLastName.text = lastName
                                                                               binding.specializationValue.text = specialization
                                                                               binding.userEmail.text = email
                                                                               binding.experienceValue.text = experience
                                                                               binding.addressValue.text = homeAddress
                                                                               binding.ageValue.text = age
                                                                           }
                                                                       }
                                                                   }
                                                               }
                                                           }
                                                       }
                                                       "patient" -> {

                                                           viewModel.getPatientProfile(email)
                                                           viewModel.patientProfileState.collectLatest { patientProfileState ->
                                                               when(patientProfileState.isLoading) {
                                                                   true -> binding.progressBar.visibility = View.VISIBLE
                                                                   false -> {
                                                                       when(patientProfileState.data) {
                                                                           null -> binding.progressBar.visibility = View.VISIBLE
                                                                           else -> {
                                                                               binding.progressBar.visibility = View.GONE
                                                                               val firstName = patientProfileState.data.first_name
                                                                               val lastName = patientProfileState.data.last_name
                                                                               val homeAddress = patientProfileState.data.address
                                                                               val age = patientProfileState.data.age.toString()
                                                                               binding.doctorTag.visibility = View.GONE
                                                                               binding.doctorFirstName.text = firstName
                                                                               binding.doctorLastName.text = lastName
                                                                               binding.specialization.visibility = View.GONE
                                                                               binding.userEmail.text = email
                                                                               binding.experience.visibility = View.GONE
                                                                               binding.addressValue.text = homeAddress
                                                                               binding.ageValue.text = age
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
}