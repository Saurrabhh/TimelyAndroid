package com.example.timely.activity

//import com.example.timely.services.MyService
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import com.example.timely.R
import com.example.timely.chat.MainChat
import com.example.timely.dataClasses.User
import com.example.timely.databinding.ActivityMainBinding
import com.example.timely.fragments.KEY
import com.example.timely.fragments.KEY.Companion.fragmentName
import com.example.timely.themes.ColorDialogCallback
import com.example.timely.themes.DialogManager.Companion.showCustomAlertDialog
import com.example.timely.themes.ThemeManager.Companion.setCustomizedThemes
import com.example.timely.themes.ThemeStorage.Companion.getThemeColor
import com.example.timely.themes.ThemeStorage.Companion.setThemeColor
import com.example.timely.utils.Utils
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.jar.Manifest


open class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "MainActivity"
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var user: User
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var toggle : ActionBarDrawerToggle
    private lateinit var database: DatabaseReference

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        user = Utils.loaddata(this)
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()

        setCustomizedThemes(this, getThemeColor(this))
        setContentView(binding.root)

        ActivityCompat.requestPermissions(
            this, arrayOf(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ), PackageManager.PERMISSION_GRANTED
        )

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        val inflater = navController.navInflater
        if(user.isteacher){

            binding.navView.menu.findItem(R.id.nav_attendance).isVisible = true
            val graph = inflater.inflate(R.navigation.nav_graph_teacher)

            navController.graph = graph
        }else{
            val graph = inflater.inflate(R.navigation.nav_graph)

            navController.graph = graph
        }
        drawerLayout = binding.drawerLayout



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

        displayNavbar()
        
        binding.profileImage.setOnClickListener {
            displayprofile()
        }

    }



    open fun displayNavbar() {

        val navView : NavigationView = binding.navView
        val toolBar = binding.toolbar
        val navname = navView.getHeaderView(0).findViewById<TextView>(R.id.nav_name)

        navname.text = user.username


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
                R.id.nav_chat -> openchat()
            }
            true
        }
    }

    // for opening chat
    private fun openchat() {
        val intent = Intent(this, MainChat::class.java)
        startActivity(intent)
    }

    private fun openattendance() {
        when(fragmentName){
            KEY().HOME ->navController.navigate(R.id.action_mainFragment_to_attendanceFragment)
            KEY().FULLTT -> navController.navigate(R.id.action_fulTTFragment_to_attendanceFragment)
            KEY().PROFILE -> navController.navigate(R.id.action_profileFragment_to_attendanceFragment)
            KEY().NOTES -> navController.navigate(R.id.action_notesFragment_to_attendanceFragment)
            KEY().TEACHER -> navController.navigate(R.id.action_teacherFragment_to_attendanceFragment)
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
                Log.d(TAG+"1", chosenColor)
                setThemeColor(applicationContext, chosenColor)
                Log.d(TAG+"2", chosenColor)
                setCustomizedThemes(applicationContext, chosenColor)
                Log.d(TAG+"3", chosenColor)
                opendashboard()

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
            KEY().TEACHER -> navController.navigate(R.id.action_teacherFragment_to_fulTTFragment)

        }
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    open fun openwebsite(link: String) {
        val url = Intent(Intent.ACTION_VIEW)
        url.data = Uri.parse(link)
        startActivity(url)
    }



    open fun logoutfun() {
        val sharedPreferences = getSharedPreferences("curruserdata", MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
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
            KEY().TEACHER -> navController.navigate(R.id.action_teacherFragment_to_profileFragment)
        }
        drawerLayout.closeDrawer(GravityCompat.START)
    }
    private fun opennotes() {
        when(fragmentName){
            KEY().HOME ->navController.navigate(R.id.action_mainFragment_to_notesFragment)
            KEY().FULLTT -> navController.navigate(R.id.action_fulTTFragment_to_notesFragment)
            KEY().PROFILE -> navController.navigate(R.id.action_profileFragment_to_notesFragment)
            KEY().ATTENDANCE -> navController.navigate(R.id.action_attendanceFragment_to_notesFragment)
            KEY().TEACHER -> navController.navigate(R.id.action_teacherFragment_to_notesFragment)
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

