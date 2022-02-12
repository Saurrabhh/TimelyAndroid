package com.example.timely.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.timely.R
import com.example.timely.databinding.ActivityMainBinding
import com.example.timely.fragments.KEY
import com.example.timely.fragments.KEY.Companion.fragmentName
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

open class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private  lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var toggle : ActionBarDrawerToggle



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        setContentView(binding.root)
        drawerLayout = binding.drawerLayout
        displayNavbar()
        if(auth.currentUser == null){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()

        }
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

    }


    open fun displayNavbar() {

        val navView : NavigationView = binding.navView
        val toolBar = binding.toolbar
        toggle = ActionBarDrawerToggle(this, drawerLayout,toolBar, R.string.menu_drawer_open, R.string.menu_drawer_close)
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

    private fun opensettings() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    private fun opencontacts() {
        startActivity(Intent(this, ContactActivity::class.java))

    }

    open fun displayfullTT() {
        if (fragmentName == KEY().HOME)
            navController.navigate(R.id.action_mainFragment_to_fulTTFragment)
        if (fragmentName == KEY().PROFILE)
            navController.navigate(R.id.action_profileFragment_to_fulTTFragment)
        if (fragmentName== KEY().NOTES)
            navController.navigate(R.id.action_notesFragment_to_fulTTFragment)
        drawerLayout.closeDrawer(GravityCompat.START)

//        startActivity(Intent(this, MainActivity::class.java))
    }

    open fun openwebsite(link: String) {
        val url = Intent(Intent.ACTION_VIEW)
        url.data = Uri.parse(link)
        startActivity(url)
    }



    open fun logoutfun() {
        auth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    open fun displayprofile() {
//        val intent = Intent(this, ProfileActivity::class.java)
//        startActivity(intent)
        if (fragmentName == KEY().HOME)
        navController.navigate(R.id.action_mainFragment_to_profileFragment)
        if (fragmentName == KEY().FULLTT)
            navController.navigate(R.id.action_fulTTFragment_to_profileFragment)
        if (fragmentName== KEY().NOTES)
            navController.navigate(R.id.action_notesFragment_to_profileFragment)
        drawerLayout.closeDrawer(GravityCompat.START)


    }
    private fun opennotes() {
        if (fragmentName == KEY().HOME)
            navController.navigate(R.id.action_mainFragment_to_notesFragment)
        if (fragmentName == KEY().FULLTT)
            navController.navigate(R.id.action_fulTTFragment_to_notesFragment)
        if (fragmentName== KEY().PROFILE)
            navController.navigate(R.id.action_profileFragment_to_notesFragment)
        drawerLayout.closeDrawer(GravityCompat.START)

    }


    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


}