package com.example.timely.utils

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.timely.dataClasses.User
import com.example.timely.fragments.MainFragment
import com.google.gson.Gson

class Utils {

    fun loaddata(ctx: Context): User {
        val sharedPreferences =
            ctx.getSharedPreferences("curruserdata", AppCompatActivity.MODE_PRIVATE)
        val gson = Gson()
        val json: String? = sharedPreferences.getString("user", null)
        Log.d("gg", json.toString())
        return gson.fromJson(json, User::class.java)
    }
}