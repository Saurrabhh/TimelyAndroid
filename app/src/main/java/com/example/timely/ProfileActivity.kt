package com.example.timely

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.timely.databinding.ActivityProfileBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        displayNavbar()

        auth = FirebaseAuth.getInstance()
        val currentemail = auth.currentUser?.email.toString()

        database = FirebaseDatabase.getInstance("https://timely-524da-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users")
        database.get().addOnSuccessListener {

            val users = it.children
            for (user in users){
                if ( user.child("email").value.toString() == currentemail ){

                    binding.MainName.text = user.child("name").value.toString()
                    binding.MainUserName.text = user.child("username").value.toString()
                    binding.MainURN.text = user.child("urn").value.toString()
                    binding.MainClass.text = user.child("rollno").value.toString()
                    binding.MainSection.text = user.child("section").value.toString()
                    binding.MainSemester.text = user.child("semester").value.toString()
                    binding.MainEmail.text = user.child("email").value.toString()
                    break
                }
            }
        }.addOnFailureListener{
            Toast.makeText(this, "Failed to fetch data. Check your connection", Toast.LENGTH_SHORT).show()
        }


        binding.Home.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
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
            }
            true
        } }

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