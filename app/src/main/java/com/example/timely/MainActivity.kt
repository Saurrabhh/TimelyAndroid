package com.example.timely

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timely.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    private lateinit var day: String
    lateinit var notificationManager: NotificationManager
    private lateinit var notificationChannel: NotificationChannel
    private lateinit var builder: Notification.Builder
    private val channelId = "i.apps.notifications"
    private val description = "Test notification"
    private lateinit var database: DatabaseReference
    private lateinit var recyclerview: RecyclerView
    private lateinit var PeriodList: ArrayList<Periods>


    private lateinit var toggle : ActionBarDrawerToggle



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()

        if(auth.currentUser == null){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()

        }

        setContentView(binding.root)


        displayNavbar()
        refreshapp()

        recyclerview = binding.recyclerview

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)
//        recyclerview.setHasFixedSize(true)

        // ArrayList of class ItemsViewModel
        PeriodList = arrayListOf()

        displayPeriodData()
        getcurrentuserdata()


    }

    private fun loaddata(): Users {
        val sharedPreferences = getSharedPreferences("sharedprefs", MODE_PRIVATE)
        val name = sharedPreferences.getString("name", null)
        val username = sharedPreferences.getString("username", null)
        val urn = sharedPreferences.getString("urn", null)
        val rollno = sharedPreferences.getString("rollno", null)
        val section = sharedPreferences.getString("section", null)
        val semester = sharedPreferences.getString("semester", null)
        val email = sharedPreferences.getString("email", null)

        return Users(name, username, urn, semester, rollno, section, email)

//        Toast.makeText(this, "saved string $savedstring", Toast.LENGTH_SHORT).show()
    }


    private fun getcurrentday(): Array<String> {
        val dayint = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)


        when (dayint) {
            1 -> day = "Sunday"
            2 -> day = "Monday"
            3 -> day = "Tuesday"
            4 -> day = "Wednesday"
            5 -> day = "Thursday"
            6 -> day = "Friday"
            7 -> day = "Saturday"
        }

        return arrayOf(day, (dayint - 2).toString())
    }

    private fun getcurrenttime(): String {
        val calender = Calendar.getInstance().time
        val timeFormatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        var time = timeFormatter.format(calender).toString()
        time = time.slice(0..4)
        return time

    }

    private fun refreshapp() {

        binding.refresh.setOnRefreshListener {
            Toast.makeText(this, "Refreshed", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)

            startActivity(intent)
            binding.refresh.isRefreshing = false
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
                R.id.nav_settings -> Toast.makeText(applicationContext,"Clicked settings", Toast.LENGTH_SHORT).show()
                R.id.nav_contact -> startActivity(Intent(this, ContactActivity::class.java))
                R.id.nav_college -> openwebsite("http://www.bitdurg.ac.in/")
                R.id.nav_erp -> openwebsite("http://20.124.220.25/Accsoft_BIT/StudentLogin.aspx")
                R.id.nav_logout -> logoutfun()
            }
            true
        } }

    private fun displayfullTT() {
        val intent = Intent(this, FullTTActivity::class.java)
        startActivity(intent)
    }

    private fun openwebsite(link: String) {
        val url = Intent(Intent.ACTION_VIEW)
        url.data = Uri.parse(link)
        startActivity(url)
    }



    private fun displayprofile() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }

    private fun displayPeriodData() {


        val user = loaddata()
        var nextclassflag = 0

//        Toast.makeText(this, d.toString(), Toast.LENGTH_SHORT).show()
        database = FirebaseDatabase.getInstance("https://timely-524da-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Timetable")
        database.child("CSE").child("Sem ${user.semester}").child("${user.section}").get().addOnSuccessListener {

            val data = getcurrentday()


            val periods = it.child(data[1]).child(data[0]).children
            var newtimeleft = 11

            for (period in periods){
                val periodno = period.child("0").value.toString()
                var time = period.child("1").value.toString()
                val subject = period.child("2").child("Subject").value.toString()


                val array = time.split("-")
                val st = convertTomili(array[0])
                val et = convertTomili(array[1])
                val currtime = convertTomili(getcurrenttime())

                if (nextclassflag==1){
                    binding.MainNextPeriod.text = subject
                    if (newtimeleft <= 10){
                        createNotification(subject, newtimeleft.toString())

                    }
                    nextclassflag = 0
                }

                if(currtime in st..et) {
                    var timeleft = ((et - currtime)- 40).toString()
                    newtimeleft = timeleft.toInt()
                    timeleft = "$timeleft mins"

                    binding.MainTime.text = timeleft
                    binding.MainCurrentClass.text = subject
                    nextclassflag = 1
                }

                time = timetoampm(time)

                val teacher = period.child("2").child("Teacher").value.toString()

                val periodobject = Periods(periodno, time, subject, teacher)

                PeriodList.add(periodobject)

                }

                recyclerview.adapter = MyAdapter(PeriodList)

            }

    }

    private fun getcurrentuserdata(){

        val currentemail = auth.currentUser?.email.toString()

        database = FirebaseDatabase.getInstance("https://timely-524da-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users")
        database.get().addOnSuccessListener {

            val users = it.children
            for (user in users) {
                if (user.child("email").value.toString() == currentemail) {

                    val sharedPreferences = getSharedPreferences("sharedprefs", MODE_PRIVATE)

                    val name = user.child("name").value.toString()
                    val username = user.child("username").value.toString()
                    val urn = user.child("urn").value.toString()
                    val rollno = user.child("rollno").value.toString()
                    val section = user.child("section").value.toString()
                    val semester = user.child("semester").value.toString()
                    val email = user.child("email").value.toString()


                    val editor = sharedPreferences.edit()
                    editor.apply{
                        putString("name", name)
                        putString("username", username)
                        putString("urn", urn)
                        putString("rollno", rollno)
                        putString("section", section)
                        putString("semester", semester)
                        putString("email", email)
                    }.apply()

                    break

                }

            }

        }

    }



    private fun convertTomili(time: String): Int {
        val arr = time.split(":")
        return (arr[0] + arr[1]).toInt()
    }

    private fun logoutfun() {
        auth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return false
    }

    private fun timetoampm(time: String): String {
        val arr = time.split("-")
        val startt = arr[0]
        val endt = arr[1]
        var newstarttime:String
        var newendtime:String

        val starttarr = startt.split(":")

        val endtarr = endt.split(":")


        if (starttarr[0].toInt() > 12){
            newstarttime = (starttarr[0].toInt() - 12).toString()
            if (newstarttime.toInt()<9){
                newstarttime = "0"+newstarttime+":"+starttarr[1]+ "pm"
            }
            else{
                newstarttime = newstarttime+":"+starttarr[1]+ "pm"
            }

        }
        else{
            newstarttime = starttarr[0]

            if (newstarttime.toInt()<9){
                newstarttime = "0"+newstarttime+":"+starttarr[1]+ "am"
            }
            else{
                newstarttime = newstarttime+":"+starttarr[1]+ "am"
            }
        }

        if (endtarr[0].toInt() > 12){
            newendtime = (endtarr[0].toInt() - 12).toString()
            if (newendtime.toInt()<9){
                newendtime = "0"+newendtime+":"+endtarr[1]+ "pm"
            }
            else{
                newendtime = newendtime+":"+endtarr[1]+ "pm"
            }
        }
        else{
            newendtime = endtarr[0]
            if (newendtime.toInt()<9){
                newendtime = "0"+newendtime+":"+endtarr[1]+ "am"
            }
            else{
                newendtime = newendtime+":"+endtarr[1]+ "am"
            }
        }



        return "$newstarttime-$newendtime"

    }



    private fun createNotification(nextPeriod: String, timeLeft: String){


//        val nextPeriod: String = getcurrenttime()
//        val timeLeft: String = getcurrenttime()


        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        //After Clicking notification come to this activity
        val intent = Intent(this, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(this, channelId)
                //.setContent(contentView)
                .setContentTitle("Next Period: $nextPeriod")
                .setContentText("Time Left: $timeLeft mins")

                .setSmallIcon(R.drawable.ic_logo)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.notification_logo))
                .setStyle(Notification.InboxStyle()
                    .addLine("Time left: $timeLeft mins")
                    .addLine("Click to see Full Time Table")
                )
                .setContentIntent(pendingIntent)
        }
        else {
            builder = Notification.Builder(this)
                //.setContent(contentView)
                .setContentTitle("Next Period: $nextPeriod")
                .setContentText("Time Left: $timeLeft mins")
                .setSmallIcon(R.drawable.ic_logo)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.notification_logo))
                .setStyle(Notification.InboxStyle()
                    .addLine("Time left: $timeLeft mins")
                    .addLine("Click to see Full Time Table")
                )
                .setContentIntent(pendingIntent)
        }
        notificationManager.notify(1234, builder.build())

    }





}