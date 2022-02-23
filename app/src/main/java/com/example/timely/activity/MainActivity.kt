package com.example.timely.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
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
import com.example.timely.themes.ColorDialogCallback
import com.example.timely.themes.DialogManager.Companion.showCustomAlertDialog
import com.example.timely.themes.ThemeManager
import com.example.timely.themes.ThemeManager.Companion.setCustomizedThemes
import com.example.timely.themes.ThemeStorage
import com.example.timely.themes.ThemeStorage.Companion.getThemeColor
import com.example.timely.themes.ThemeStorage.Companion.setThemeColor
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        setCustomizedThemes(this, getThemeColor(this))
        setContentView(binding.root)
        drawerLayout = binding.drawerLayout
        displayNavbar()
        if(auth.currentUser == null){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        if (getThemeColor(this).equals("blue")) {
            window.statusBarColor = resources.getColor(R.color.colorPrimary)
        }
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        if (auth.currentUser?.email.toString() == "shubhankartiwary11@gmail.com"){
            navController.navigate(R.id.action_mainFragment_to_teacherFragment)
        }


//        binding.navView.setBackgroundColor(resources.getColor(R.color.Purple2))


    }


    open fun displayNavbar() {

        val navView : NavigationView = binding.navView
        val toolBar = binding.toolbar
        val sharedPreferences = this.getSharedPreferences("theme_data", Context.MODE_PRIVATE)
        val a = sharedPreferences.getString("theme", "grey")
        when(a){
            "pink" -> toolBar.setBackgroundColor(resources.getColor(R.color.Pink2))
            "red" -> toolBar.setBackgroundColor(resources.getColor(R.color.Red2))
            "blue" -> toolBar.setBackgroundColor(resources.getColor(R.color.Blue1))
            "green" -> toolBar.setBackgroundColor(resources.getColor(R.color.Green2))
            "purple" -> toolBar.setBackgroundColor(resources.getColor(R.color.Purple2))
            "yellow" -> toolBar.setBackgroundColor(resources.getColor(R.color.Yellow2))
            "grey" -> toolBar.setBackgroundColor(resources.getColor(R.color.Grey2))
            "orange" -> toolBar.setBackgroundColor(resources.getColor(R.color.Orange2))
        }

        toggle = ActionBarDrawerToggle(this, drawerLayout,toolBar, R.string.menu_drawer_open, R.string.menu_drawer_close)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_profile -> displayprofile()
                R.id.nav_timetable -> displayfullTT()
                R.id.nav_settings -> opensettings()
                R.id.nav_theme -> changeTheme()

                R.id.nav_contact -> opencontacts()
                R.id.nav_college -> openwebsite("http://www.bitdurg.ac.in/")
                R.id.nav_erp -> openwebsite("http://20.124.220.25/Accsoft_BIT/StudentLogin.aspx")
                R.id.nav_logout -> logoutfun()
                 R.id.notes -> opennotes()
            }
            true
        } }

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