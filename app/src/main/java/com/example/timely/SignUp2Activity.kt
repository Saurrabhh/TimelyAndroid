package com.example.timely

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.timely.data.UserViewMOdel
import com.example.timely.databinding.ActivitySignUp2Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class SignUp2Activity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUp2Binding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    lateinit var viewModel: UserViewMOdel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUp2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        binding.SubmitBtn1.setOnClickListener {
            createnewuser()
        }

    }

    private fun createnewuser() {

        val name = binding.InputName1.text.toString()
        val username = binding.InputUsername1.text.toString()
        val urn = binding.InputURN1.text.toString()
        val semester = binding.InputSem1.text.toString()
        val section = binding.InputSection1.text.toString().uppercase(Locale.getDefault())
        val rollno = binding.InputRollNO1.text.toString()
        val email = intent.getStringExtra("email").toString()
        val password = intent.getStringExtra("password").toString()




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
                        finish()

                    } else {
                        Toast.makeText(this, "Registration Failed", Toast.LENGTH_LONG).show()
                    }
            }
        }
    }
}






