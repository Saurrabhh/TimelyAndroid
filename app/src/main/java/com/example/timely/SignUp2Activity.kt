package com.example.timely

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.timely.databinding.ActivitySignUp2Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp2Activity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUp2Binding
    private lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUp2Binding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.SubmitBtn1.setOnClickListener{

            val name = binding.InputName1.text.toString()
            val username = binding.InputUsername1.text.toString()
            val urn = binding.InputURN1.text.toString()
            val semester = binding.InputSem1.text.toString()
            val section = binding.InputSection1.text.toString()
            val rollno = binding.InputRollNO1.text.toString()
            val email = intent.getStringExtra("email")


            val user = Users(name, username, urn, semester, rollno, section, email)
            database = FirebaseDatabase.getInstance("https://timely-524da-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users")
            database.child(username).setValue(user).addOnSuccessListener {
                Toast.makeText(this, "You are now Registered", Toast.LENGTH_SHORT).show()
            }
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}