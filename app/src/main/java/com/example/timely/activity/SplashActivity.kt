package com.example.timely.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.TextView
import java.lang.Thread
import java.lang.Exception
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.WindowInsets
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
import com.example.timely.R
import com.example.timely.dataClasses.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder


class SplashActivity : AppCompatActivity() {


    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        auth = FirebaseAuth.getInstance()

        supportActionBar?.hide()

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }



        val textView = findViewById<TextView>(R.id.textSplash)
        val ani = findViewById<LottieAnimationView>(R.id.lottieAnimationView)

        val info = findViewById<TextView>(R.id.info)
        val name1 = findViewById<TextView>(R.id.name1)
        val name2 = findViewById<TextView>(R.id.name2)
        val name3 = findViewById<TextView>(R.id.name3)
        val name4 = findViewById<TextView>(R.id.name4)


        getcurrentuserdata()



        textView.animate().translationX(1000f).setDuration(1000).startDelay = 2500
        ani.animate().translationX(-1000f).setDuration(1000).startDelay = 2500

        info.animate().translationY(1000f).setDuration(1000).startDelay = 2500

        name1.animate().translationX(-1000f).setDuration(1000).startDelay = 2500
        name3.animate().translationX(-1000f).setDuration(1000).startDelay = 2500

        name2.animate().translationX(1000f).setDuration(1000).startDelay = 2500
        name4.animate().translationX(1000f).setDuration(1000).startDelay = 2500


        val thread: Thread = object : Thread() {
            override fun run() {
                try {
                    sleep(3500)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
        thread.start()
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