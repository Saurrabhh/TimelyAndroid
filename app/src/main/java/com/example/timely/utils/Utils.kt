package com.example.timely.utils

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.timely.dataClasses.User
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class Utils {

    companion object{

        fun loaddata(ctx: Context): User {
            val sharedPreferences =
                ctx.getSharedPreferences("curruserdata", AppCompatActivity.MODE_PRIVATE)
            val gson = Gson()
            val json: String? = sharedPreferences.getString("user", null)
            Log.d("gg", json.toString())
            val g = GsonBuilder().create().fromJson(json, User::class.java)


            return g    }
    }
}