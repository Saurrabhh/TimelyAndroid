package com.example.timely.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.timely.R
import com.example.timely.databinding.ActivitySignUpBinding
import com.example.timely.themes.ThemeManager
import com.example.timely.themes.ThemeStorage
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        ThemeManager.setCustomizedThemes(this, ThemeStorage.getThemeColor(this))
        setContentView(binding.root)

        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        if (ThemeStorage.getThemeColor(this).equals("blue")) {
            window.statusBarColor = resources.getColor(R.color.colorPrimary)
        }

        auth = FirebaseAuth.getInstance()

        binding.NextBtn.setOnClickListener{

            val email: String = binding.Inputmail.text.toString().trim()
            val password: String = binding.InputPass.text.toString().trim()
            val confpassword: String = binding.InputConfirmPass.text.toString().trim()


            if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confpassword)) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            }
            else if (!TextUtils.equals(password, confpassword)){
                Toast.makeText(this, "Enter same password", Toast.LENGTH_SHORT).show()
            }else{


                val intent = Intent(this, SignUp2Activity::class.java)
                intent.putExtra("email", email)
                intent.putExtra("password", password)
                startActivity(intent)

            }

        }
    }
}