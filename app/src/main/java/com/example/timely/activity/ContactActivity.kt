package com.example.timely.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.example.timely.databinding.ActivityContactBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class ContactActivity : AppCompatActivity() {

    private lateinit var toggle : ActionBarDrawerToggle
    private lateinit var auth : FirebaseAuth

    private lateinit var mail: ImageButton
    private lateinit var insta: ImageButton
    private lateinit var linkedin: ImageButton
    private lateinit var binding: ActivityContactBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactBinding.inflate(layoutInflater)
        setContentView(binding.root)


        auth = FirebaseAuth.getInstance()


        //      For Card 1
        mail = binding.mail1
        insta = binding.insta1
        linkedin = binding.in1

        mail.setOnClickListener {
            openwebsite("mailto:saurabhkyadav99@gmail.com")
        }
        insta.setOnClickListener {
            openwebsite("https://www.instagram.com/sk_yadav.06/")

        }
        linkedin.setOnClickListener {
            openwebsite("https://www.linkedin.com/in/saurabh-yadav-73616b137/")
        }

//      For Card 2
        mail = binding.mail2
        insta = binding.insta2
        linkedin = binding.in2

        mail.setOnClickListener {
            openwebsite("mailto:shubhankartiwary10@gmail.com")
        }
        insta.setOnClickListener {
            openwebsite("https://www.instagram.com/shubhankar_tiwary10/")

        }
        linkedin.setOnClickListener {
            openwebsite("https://www.linkedin.com/in/shubhankar10/")
        }

//      For Card 3
        mail = binding.mail3
        insta = binding.insta3
        linkedin = binding.in3

        mail.setOnClickListener {
            openwebsite("mailto:cyrao378@gmail.com")
        }
        insta.setOnClickListener {
            openwebsite("https://www.instagram.com/mr.rao2110/")

        }
        linkedin.setOnClickListener {
            openwebsite("https://www.linkedin.com/in/chaitanya-rao-375b30207")
        }


//      For Card 4
        mail = binding.mail4
        insta = binding.insta4
        linkedin = binding.in4

        mail.setOnClickListener {
            openwebsite("mailto:krishchopra22@gmail.com")
        }
        insta.setOnClickListener {
            openwebsite("https://www.instagram.com/__s_e_c_r_e_t__a_r_t_i_s_t__/")

        }
        linkedin.setOnClickListener {
            openwebsite("https://www.linkedin.com/in/krishchopra22")
        }

    }




    private fun openwebsite(link: String) {
        val url = Intent(Intent.ACTION_VIEW)
        url.data = Uri.parse(link)
        startActivity(url)
    }



}