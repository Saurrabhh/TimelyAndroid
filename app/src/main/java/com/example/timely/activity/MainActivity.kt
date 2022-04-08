package com.example.timely.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import com.example.timely.R
import com.example.timely.databinding.ActivityMainBinding
import com.example.timely.fragments.AttendanceFragment
import com.example.timely.fragments.KEY
import com.example.timely.fragments.KEY.Companion.fragmentName
//import com.example.timely.services.MyService
import com.example.timely.themes.ColorDialogCallback
import com.example.timely.themes.DialogManager.Companion.showCustomAlertDialog
import com.example.timely.themes.ThemeManager.Companion.setCustomizedThemes
import com.example.timely.themes.ThemeStorage.Companion.getThemeColor
import com.example.timely.themes.ThemeStorage.Companion.setThemeColor
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase

open class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "MainActivity"
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private  lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var toggle : ActionBarDrawerToggle

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        setCustomizedThemes(this, getThemeColor(this))
        setContentView(binding.root)
        drawerLayout = binding.drawerLayout
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        navController = navHostFragment.navController

        if(auth.currentUser == null){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        else if(auth.currentUser?.email.toString() == "shubhankartiwary11@gmail.com"){

            navController.navigate(R.id.action_mainFragment_to_teacherFragment)

        }
        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        if (getThemeColor(this).equals("blue")) {
            window.statusBarColor = resources.getColor(R.color.colorPrimary)
        }




        displayNavbar()


//       val database1 = FirebaseDatabase.getInstance("https://timely-524da-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users")
//        database1.get().addOnSuccessListener {
//            Toast.makeText(this, it.children.toString(), Toast.LENGTH_SHORT).show()
////            val a = it.child()
////            Toast.makeText(this, a.toString(), Toast.LENGTH_SHORT).show()
//        }




//        val intent = Intent(this, MyService::class.java)
//        startService(intent)


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
                R.id.nav_home-> opendashboard()
                R.id.nav_profile -> displayprofile()
                R.id.nav_timetable -> displayfullTT()
                R.id.nav_settings -> opensettings()
                R.id.nav_theme -> changeTheme()
                R.id.nav_contact -> opencontacts()
                R.id.nav_college -> openwebsite("http://www.bitdurg.ac.in/")
                R.id.nav_erp -> openwebsite("http://20.124.220.25/Accsoft_BIT/StudentLogin.aspx")
                R.id.nav_logout -> logoutfun()
                R.id.notes -> opennotes()
                R.id.nav_attendance -> openattendance()
            }
            true
        }
    }

    private fun openattendance() {
        when(fragmentName){
            KEY().HOME ->navController.navigate(R.id.action_mainFragment_to_attendanceFragment)
            KEY().FULLTT -> navController.navigate(R.id.action_fulTTFragment_to_attendanceFragment)
            KEY().PROFILE -> navController.navigate(R.id.action_profileFragment_to_attendanceFragment)
            KEY().NOTES -> navController.navigate(R.id.action_notesFragment_to_attendanceFragment)
        }
        drawerLayout.closeDrawer(GravityCompat.START)
    }


    private fun changeTheme() {
        showCustomAlertDialog(this, object : ColorDialogCallback {
            override fun onChosen(chosenColor: String) {
                if (chosenColor == getThemeColor(applicationContext)) {
                    Toast.makeText(this@MainActivity,
                        "Theme is already chosen",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                Log.d(TAG, chosenColor)
                setThemeColor(applicationContext, chosenColor)
                setCustomizedThemes(applicationContext, chosenColor)
                recreate()
            }
        })
    }
    private fun opensettings() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    private fun opendashboard() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
    private fun opencontacts() {
        startActivity(Intent(this, ContactActivity::class.java))
    }

    open fun displayfullTT() {
        when(fragmentName){
            KEY().HOME -> navController.navigate(R.id.action_mainFragment_to_fulTTFragment)
            KEY().PROFILE -> navController.navigate(R.id.action_profileFragment_to_fulTTFragment)
            KEY().NOTES -> navController.navigate(R.id.action_notesFragment_to_fulTTFragment)
            KEY().ATTENDANCE -> navController.navigate(R.id.action_attendanceFragment_to_fulTTFragment)

        }
        drawerLayout.closeDrawer(GravityCompat.START)
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
        when(fragmentName){
            KEY().HOME -> navController.navigate(R.id.action_mainFragment_to_profileFragment)
            KEY().FULLTT -> navController.navigate(R.id.action_fulTTFragment_to_profileFragment)
            KEY().NOTES -> navController.navigate(R.id.action_notesFragment_to_profileFragment)
            KEY().ATTENDANCE -> navController.navigate(R.id.action_attendanceFragment_to_profileFragment)
        }
        drawerLayout.closeDrawer(GravityCompat.START)
    }
    private fun opennotes() {
        when(fragmentName){
            KEY().HOME ->navController.navigate(R.id.action_mainFragment_to_notesFragment)
            KEY().FULLTT -> navController.navigate(R.id.action_fulTTFragment_to_notesFragment)
            KEY().PROFILE -> navController.navigate(R.id.action_profileFragment_to_notesFragment)
            KEY().ATTENDANCE -> navController.navigate(R.id.action_attendanceFragment_to_notesFragment)
        }
        drawerLayout.closeDrawer(GravityCompat.START)
    }
    @ColorInt
    fun getThemeColor(@AttrRes attributeColor: Int): Int {
        val value = TypedValue()
        theme.resolveAttribute(attributeColor, value, true)
        return value.data
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

}

