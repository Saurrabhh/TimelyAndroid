package com.example.timely.utils

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.timely.dataClasses.User

class Utils {

    companion object{

        fun loaddata(ctx: Context): User {
            val sharedPreferences =
                ctx.getSharedPreferences("curruserdata", AppCompatActivity.MODE_PRIVATE)

            val uid: String? = sharedPreferences.getString("uid", null)
            val name: String? = sharedPreferences.getString("name", null)
            val username: String? = sharedPreferences.getString("username", null)
            val urn: String? = sharedPreferences.getString("urn", null)
            val semester: String? = sharedPreferences.getString("semester", null)
            val rollno: String? = sharedPreferences.getString("rollno", null)
            val section: String? = sharedPreferences.getString("section", null)
            val email: String? = sharedPreferences.getString("email", null)
            val gender: String? = sharedPreferences.getString("gender", null)
            val branch: String? = sharedPreferences.getString("branch", null)
            val phoneno: String? = sharedPreferences.getString("phoneno", null)
            val enroll: String? = sharedPreferences.getString("enroll", null)
            val isteacher: Boolean = sharedPreferences.getBoolean("isteacher", false)


            return User(
                uid,
                name,
                username,
                urn,
                semester,
                rollno,
                section,
                email,
                gender,
                branch,
                phoneno,
                enroll,
                isteacher
            )
        }
    }
}