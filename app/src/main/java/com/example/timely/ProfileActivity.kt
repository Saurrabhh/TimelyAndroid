package com.example.timely

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.timely.databinding.ActivityProfileBinding
import com.example.timely.databinding.ActivitySignUp2Binding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}