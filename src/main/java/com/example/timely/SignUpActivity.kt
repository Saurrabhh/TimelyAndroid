package com.example.timely

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var signupbtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        auth = FirebaseAuth.getInstance()
        email = findViewById(R.id.InputUser)
        password = findViewById(R.id.InputPass)
        signupbtn= findViewById(R.id.SignUpBtn)

        signupbtn.setOnClickListener{
            val inpemail: String = email.text.toString()
            val inppass: String = password.text.toString()

            if(TextUtils.isEmpty(inpemail) || TextUtils.isEmpty(inppass)) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_LONG).show()
            } else{
                auth.createUserWithEmailAndPassword(inpemail, inppass).addOnCompleteListener(this
                ) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Successfully Registered", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Registration Failed", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}