package com.example.healthinfo.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.healthinfo.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    lateinit var binding:ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(this.layoutInflater)
        setContentView(binding.root)
    }
}