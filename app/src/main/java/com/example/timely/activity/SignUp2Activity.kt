package com.example.timely.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.timely.R
import com.example.timely.dataClasses.Users
import com.example.timely.databinding.ActivitySignUp2Binding
import com.example.timely.themes.ThemeManager
import com.example.timely.themes.ThemeStorage
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
        ThemeManager.setCustomizedThemes(this, ThemeStorage.getThemeColor(this))
        setContentView(binding.root)

        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        if (ThemeStorage.getThemeColor(this).equals("blue")) {
            window.statusBarColor = resources.getColor(R.color.colorPrimary)
        }
        auth = FirebaseAuth.getInstance()
//        val email = intent.getStringExtra("email").toString().trim()
//        val password = intent.getStringExtra("password").toString().trim()
//        Toast.makeText(this, "$email $password", Toast.LENGTH_SHORT).show()


        binding.SubmitBtn.setOnClickListener {
            createnewuser()
        }

        val semesters = resources.getStringArray(R.array.semesters)
        val sections: Array<String> = arrayOf("A","B")
        val genders: Array<String> = arrayOf("Male","Female","Others")
        val branch: Array<String> =arrayOf("CSE","IT","Mech","Civil")

        val arrayAdapter = ArrayAdapter(this, R.layout.dropdowntext, semesters)
        val arrayAdapter1 = ArrayAdapter(this@SignUp2Activity, R.layout.dropdowntext, sections)
        val arrayAdapter2 = ArrayAdapter(this@SignUp2Activity, R.layout.dropdowntext, genders)
        val arrayAdapter3 = ArrayAdapter(this@SignUp2Activity, R.layout.dropdowntext, branch)

        binding.Inputsem.threshold = 0
        binding.Inputsec.threshold = 0
        binding.InputGender.threshold = 0
        binding.Inputbranch.threshold = 0

        binding.Inputsem.setAdapter(arrayAdapter)
        binding.Inputsec.setAdapter(arrayAdapter1)
        binding.InputGender.setAdapter(arrayAdapter2)
        binding.Inputbranch.setAdapter(arrayAdapter3)


        binding.Inputbranch.setOnFocusChangeListener { v, hasFocus ->

            if (hasFocus){
                binding.Inputbranch.showDropDown()
            }

        }
        binding.Inputsem.setOnFocusChangeListener { v, hasFocus ->

            if (hasFocus){
                binding.Inputsem.showDropDown()
            }

        }

        binding.InputGender.setOnFocusChangeListener { v, hasFocus ->

            if (hasFocus){
                binding.InputGender.showDropDown()
            }

        }


        binding.Inputsec.setOnFocusChangeListener { v, hasFocus ->

            if (hasFocus){
                binding.Inputsec.showDropDown()
            }
            else{
                binding.Inputsec.dismissDropDown()
            }

        }



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

        val name = binding.Inputname.text.toString().trim()
        val username = binding.Inputusername.text.toString().trim()
        val urn = binding.InputUrn.text.toString().trim()
        val semester = binding.Inputsem.text.toString().trim()
//        val section = binding.Inputsec.text.toString().uppercase(Locale.getDefault()).trim()
        val section = binding.Inputsec.text.toString().trim()
        val rollno = binding.InputclassRoll.text.toString().trim()
        val gender = binding.InputGender.text.toString().trim()
        val email = intent.getStringExtra("email").toString().trim()
        val password = intent.getStringExtra("password").toString().trim()
        val branch = binding.Inputbranch.text.toString().trim()


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
        else if (TextUtils.isEmpty(section)){
            Toast.makeText(this, "Please Enter your Section", Toast.LENGTH_SHORT).show()
        }
        else if (TextUtils.isEmpty(rollno)){
            Toast.makeText(this, "Please Enter your Roll No", Toast.LENGTH_SHORT).show()
        }
        else if (TextUtils.isEmpty(gender)){
            Toast.makeText(this, "Please Enter your Gender", Toast.LENGTH_SHORT).show()
        }
        else if (rollno.toInt() <= 0){
            Toast.makeText(this, "Please Enter a valid Roll No", Toast.LENGTH_SHORT).show()
        }
        else if (TextUtils.isEmpty(branch)){
            Toast.makeText(this, "Please Enter your Branch", Toast.LENGTH_SHORT).show()
        }

        else{

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) {

                task ->
                    if (task.isSuccessful) {

                        val user = Users(name, username, urn, semester, rollno, section, email, gender)
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








