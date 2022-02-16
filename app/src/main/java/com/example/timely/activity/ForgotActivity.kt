package com.example.timely.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Toast
import com.example.timely.R
import com.example.timely.databinding.ActivityForgotBinding
import com.example.timely.themes.ThemeManager
import com.example.timely.themes.ThemeStorage
import com.google.firebase.auth.FirebaseAuth

class ForgotActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotBinding
    private lateinit var auth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotBinding.inflate(layoutInflater)
        ThemeManager.setCustomizedThemes(this, ThemeStorage.getThemeColor(this))
        setContentView(binding.root)

        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        if (ThemeStorage.getThemeColor(this).equals("blue")) {
            window.statusBarColor = resources.getColor(R.color.colorPrimary)
        }

        auth = FirebaseAuth.getInstance()


        binding.ResetBtn.setOnClickListener{
            val email = binding.InputEmail1.text.toString()
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show()
            }
            else {
                auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Mail Sent", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Failed to send Mail", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener{
                    Toast.makeText(this, "Failed to send Mail", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}