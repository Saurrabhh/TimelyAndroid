package com.example.timely.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.timely.R
import com.example.timely.dataClasses.Users
import com.example.timely.databinding.ActivitySignUp2Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class SignUp2Activity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUp2Binding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUp2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()


        binding.SubmitBtn1.setOnClickListener {
            createnewuser()
        }

        val semesters = resources.getStringArray(R.array.semesters)
        var sections: Array<String> = arrayOf("A","B")

        val arrayAdapter = ArrayAdapter(this, R.layout.dropdowntext, semesters)
        val arrayAdapter1 = ArrayAdapter(this@SignUp2Activity, R.layout.dropdowntext, sections)

        binding.InputSem1.setAdapter(arrayAdapter)
        binding.InputSection1.setAdapter(arrayAdapter1)

//        binding.InputSem1?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//
//            }
//
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                val sem = binding.InputSem1.text.toString()
//
//                when(sem){
//
//                    "3" -> sections = arrayOf("E","F")
//                    "5" -> sections = arrayOf("A","B")
//                    "7" -> sections = arrayOf("A","B")
//                }
//                val arrayAdapter1 = ArrayAdapter(this@SignUp2Activity, R.layout.dropdowntext, sections)
//                binding.InputSection1.setAdapter(arrayAdapter1)
////
//            }
//
//        }


    }

    private fun createnewuser() {

        val name = binding.InputName1.text.toString().trim()
        val username = binding.InputUsername1.text.toString().trim()
        val urn = binding.InputURN1.text.toString().trim()
        val semester = binding.InputSem1.text.toString().trim()
        val section = binding.InputSection1.text.toString().uppercase(Locale.getDefault()).trim()
        val rollno = binding.InputRollNO1.text.toString().trim()
        val email = intent.getStringExtra("email").toString().trim()
        val password = intent.getStringExtra("password").toString().trim()




        if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please Enter your Name", Toast.LENGTH_SHORT).show()
        }
        else if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Please Enter your Username", Toast.LENGTH_SHORT).show()
        }
        else if (TextUtils.isEmpty(urn)){
            Toast.makeText(this, "Please Enter your URN", Toast.LENGTH_SHORT).show()
        }
        else if (TextUtils.getTrimmedLength(urn) != 12){
            Toast.makeText(this, "Please Enter a valid URN", Toast.LENGTH_SHORT).show()
        }
        else if (TextUtils.isEmpty(semester)){
            Toast.makeText(this, "Please Enter your Semester", Toast.LENGTH_SHORT).show()
        }
        else if (semester !in "1 2 3 4 5 6 7 8"){
            Toast.makeText(this, "Please Enter a valid Semester", Toast.LENGTH_SHORT).show()
        }
        else if (TextUtils.isEmpty(section)){
            Toast.makeText(this, "Please Enter your Section", Toast.LENGTH_SHORT).show()
        }
        else if (TextUtils.isEmpty(rollno)){
            Toast.makeText(this, "Please Enter your Roll No", Toast.LENGTH_SHORT).show()
        }
        else if (rollno.toInt() <= 0){
            Toast.makeText(this, "Please Enter a valid Roll No", Toast.LENGTH_SHORT).show()
        }

        else{

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) {

                task ->
                    if (task.isSuccessful) {

                        val user = Users(name, username, urn, semester, rollno, section, email)
                        database =
                            FirebaseDatabase.getInstance("https://timely-524da-default-rtdb.asia-southeast1.firebasedatabase.app/")
                                .getReference("Users")

                        database.child(username).setValue(user).addOnSuccessListener {
                            Toast.makeText(this, "Successfully Registered", Toast.LENGTH_LONG).show()

                        }
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finishAffinity()

                    } else {
                        Toast.makeText(this, "Registration Failed", Toast.LENGTH_LONG).show()
                    }
            }
        }
    }
}






