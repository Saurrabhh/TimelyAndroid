package com.example.timely

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var email: EditText
    private lateinit var password: EditText

    private lateinit var loginbtn: Button
    private lateinit var signupbtn: Button
    private lateinit var forgotbtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        email = findViewById(R.id.InputUser)
        password = findViewById(R.id.InputPass
        )
        loginbtn= findViewById(R.id.LogInBtn)
        signupbtn = findViewById(R.id.SignUpBtn)
        forgotbtn = findViewById(R.id.Forgot)

        auth = FirebaseAuth.getInstance()

        loginbtn.setOnClickListener{
            val inpemail: String = email.text.toString()
            val inppass: String = password.text.toString()

            if(TextUtils.isEmpty(inpemail) || TextUtils.isEmpty(inppass)) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_LONG).show()
            } else{
                auth.signInWithEmailAndPassword(inpemail, inppass).addOnCompleteListener(this, OnCompleteListener{ task ->
                    if(task.isSuccessful){
                        Toast.makeText(this, "Successfully Logged In", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else {
                        Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show()
                    }
                })
            }
        }

        signupbtn.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)

        }

        forgotbtn.setOnClickListener{
            val intent = Intent(this, ForgotActivity::class.java )
            startActivity(intent)
        }
    }
}