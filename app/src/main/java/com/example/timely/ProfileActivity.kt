package com.example.timely

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.timely.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        val currentemail = auth.currentUser?.email.toString()

        database = FirebaseDatabase.getInstance("https://timely-524da-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users")
        database.get().addOnSuccessListener {

            val users = it.children
            for (user in users){
                if ( user.child("email").value.toString() == currentemail ){

                    binding.MainName.text = user.child("name").value.toString()
                    binding.MainUserName.text = user.child("username").value.toString()
                    binding.MainURN.text = user.child("urn").value.toString()
                    binding.MainClass.text = user.child("rollno").value.toString()
                    binding.MainSection.text = user.child("section").value.toString()
                    binding.MainSemester.text = user.child("semester").value.toString()
                    binding.MainEmail.text = user.child("email").value.toString()
                    break
                }
            }
        }


        binding.Home.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }



    }
}