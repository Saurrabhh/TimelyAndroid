package com.example.timely

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.timely.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.NextBtn.setOnClickListener{

            val email: String = binding.InputEmail.text.toString()
            val password: String = binding.InputPass.text.toString()
            val confpassword: String = binding.InputConfirmPass.text.toString()


            if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confpassword)) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_LONG).show()
            }
            else if (!TextUtils.equals(password, confpassword)){
                Toast.makeText(this, "Enter same password", Toast.LENGTH_LONG).show()
            }else{
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this
                ) { task ->
                    if (task.isSuccessful) {

                        Toast.makeText(this, "Successfully Registered", Toast.LENGTH_LONG).show()

                        val intent = Intent(this, SignUp2Activity::class.java)
                        intent.putExtra("email",email)
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