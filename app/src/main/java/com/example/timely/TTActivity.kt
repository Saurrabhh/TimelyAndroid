package com.example.timely

import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.timely.databinding.ActivityTtactivityBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class TTActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTtactivityBinding
    private lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTtactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance("https://timely-524da-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Timetable")
        database.child("CSE").child("Sem 3").child("E").get().addOnSuccessListener {
            val periods = it.child("Monday").children

            for (period in periods){
                val no = period.child("0").value.toString()
                val time = period.child("1").value.toString()
                val sub = period.child("2").child("Subject").value.toString()
                val prof = period.child("2").child("Teacher").value.toString()
                Toast.makeText(this, "$no $time $sub $prof", Toast.LENGTH_SHORT).show()
            }

        }


    }
}