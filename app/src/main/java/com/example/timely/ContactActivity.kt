package com.example.timely

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


        displayNavbar()

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



    private fun displayNavbar() {
        val drawerLayout : DrawerLayout = binding.drawerLayout
        val navView : NavigationView = binding.navView

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.menu_drawer_open, R.string.menu_drawer_close)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_profile -> displayprofile()
                R.id.nav_timetable -> displayfullTT()
                R.id.nav_settings -> opensettings()
                R.id.nav_contact -> opencontacts()
                R.id.nav_college -> openwebsite("http://www.bitdurg.ac.in/")
                R.id.nav_erp -> openwebsite("http://20.124.220.25/Accsoft_BIT/StudentLogin.aspx")
                R.id.nav_logout -> logoutfun()
                R.id.notes -> opennotes()
            }
            true
        } }

    private fun opennotes() {
        val intent = Intent(this, NotesActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun opensettings() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun displayfullTT() {
        val intent = Intent(this, FullTTActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun opencontacts() {
        startActivity(Intent(this, ContactActivity::class.java))
        finish()

    }

    private fun openwebsite(link: String) {
        val url = Intent(Intent.ACTION_VIEW)
        url.data = Uri.parse(link)
        startActivity(url)
    }



    private fun logoutfun() {
        auth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun displayprofile() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
        finish()
    }



}