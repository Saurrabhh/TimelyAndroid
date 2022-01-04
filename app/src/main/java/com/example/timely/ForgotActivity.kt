package com.example.timely

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class ForgotActivity : AppCompatActivity() {

    private lateinit var Btn: Button
    private lateinit var email: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)

        Btn = findViewById(R.id.EmailBtn)
        email = findViewById(R.id.InputEmail1)

        Btn.setOnClickListener{
            val mail: String = email.text.toString()
            if (TextUtils.isEmpty(mail)) {
                Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(this, "Mail Sent", Toast.LENGTH_LONG).show()
            }
            //Toast.makeText(this, "Mail Sent", Toast.LENGTH_LONG).show()
        }

    }
}