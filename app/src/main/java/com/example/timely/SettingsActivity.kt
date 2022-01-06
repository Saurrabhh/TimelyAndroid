package com.example.timely

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SwitchCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.timely.databinding.ActivitySettingsBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class SettingsActivity : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var notifSwitch : SwitchCompat
    private lateinit var miliSwitch : SwitchCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        displayNavbar()

        notifSwitch = binding.notificationSwitch as SwitchCompat
        miliSwitch = binding.militaryswitch as SwitchCompat
        notifSwitch.setOnClickListener {
            if (notifSwitch.isChecked) {
                Toast.makeText(this@SettingsActivity, "Notification turned ON", Toast.LENGTH_SHORT).show()

            }
            else{
                Toast.makeText(this@SettingsActivity, "Notification turned OFF", Toast.LENGTH_SHORT).show()
            }
        }

        miliSwitch.setOnClickListener{
            if (miliSwitch.isChecked) {
                Toast.makeText(this@SettingsActivity, "Done", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("gomili", "yes")
                startActivity(intent)
                finish()
            }
            else{
                Toast.makeText(this@SettingsActivity, "OFF", Toast.LENGTH_SHORT).show()
            }
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