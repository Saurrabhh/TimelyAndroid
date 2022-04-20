package com.example.timely.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.timely.R
import com.example.timely.dataClasses.User
import com.example.timely.databinding.ActivityLoginBinding
import com.example.timely.themes.ThemeManager
import com.example.timely.themes.ThemeStorage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    private lateinit var database: DatabaseReference


    @RequiresApi(Build.VERSION_CODES.N_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        ThemeManager.setCustomizedThemes(this, ThemeStorage.getThemeColor(this))
        setContentView(binding.root)

        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        if (ThemeStorage.getThemeColor(this).equals("blue")) {
            window.statusBarColor = resources.getColor(R.color.colorPrimary)
        }


        auth = FirebaseAuth.getInstance()


        if(auth.currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()

        }
        

        binding.LogInBtn.setOnClickListener{
            val email: String = binding.InputEmail.text.toString().trim()
            val password: String = binding.InputPass.text.toString().trim()

            if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_LONG).show()
            } else{
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this
                ) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Successfully Logged In", Toast.LENGTH_LONG).show()
                        getcurrentuserdata()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        binding.SignUpBtn.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)

        }

        binding.ForgotBtn.setOnClickListener{
            val intent = Intent(this, ForgotActivity::class.java )
            startActivity(intent)
        }
    }
    private fun getcurrentuserdata() {
        database = FirebaseDatabase.getInstance("https://timely-524da-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users")

        auth.uid?.let { uidd ->
            database.child(uidd).get().addOnSuccessListener {
                val sharedPreferences = getSharedPreferences("curruserdata", MODE_PRIVATE)
                val user = it.getValue(User::class.java)
                val json = GsonBuilder().create().toJson(user)
                Log.d(MainActivity.TAG, json)
                val editor = sharedPreferences.edit()
                editor.apply {
                    putString("uid", user!!.uid)
                    putString("name", user.name)
                    putString("username", user.username)
                    putString("urn", user.urn)
                    putString("semester", user.semester)
                    putString("rollno", user.rollno)
                    putString("section", user.section)
                    putString("email", user.email)
                    putString("gender", user.gender)
                    putString("branch", user.branch)
                    putString("phoneno", user.phoneno)
                    putString("enroll", user.enroll)
                    putBoolean("isteacher", user.isteacher)
                }.apply()
            }.addOnFailureListener{
                Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}