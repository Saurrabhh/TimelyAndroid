package com.example.timely.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.TextView
import java.lang.Thread
import java.lang.Exception
import android.content.Intent
import android.os.Build
import android.view.WindowInsets
import com.airbnb.lottie.LottieAnimationView
import com.example.timely.R


class SplshActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar!!.hide()

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
                    val intent = Intent(this@SplshActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
        thread.start()
    }
}