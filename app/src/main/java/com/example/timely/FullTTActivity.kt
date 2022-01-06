package com.example.timely

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timely.databinding.ActivityFullTtactivityBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FullTTActivity : AppCompatActivity() {


    private lateinit var binding: ActivityFullTtactivityBinding
    private lateinit var DayList: ArrayList<DayPeriod>
    private lateinit var database: DatabaseReference
    private lateinit var recyclerview: RecyclerView
    private lateinit var toggle1 : ActionBarDrawerToggle
    private lateinit var auth : FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullTtactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)





        auth = FirebaseAuth.getInstance()

        displayNavbar()

        refreshapp()



        // getting the recyclerview by its id
        recyclerview = binding.fullTTRecycler

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)

        // ArrayList of class ItemsViewModel
        DayList = arrayListOf()

        displayfulltt()



    }

    private fun displayfulltt() {

        val user = loaddata()
        database = FirebaseDatabase.getInstance("https://timely-524da-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Timetable")
        database.child("CSE").child("Sem ${user.semester}").child("${user.section}").get().addOnSuccessListener {

            val days = it.children
            val list = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
            var i = 0

            val times = it.child("0").child("Monday")

            val temp = "Time"

            val period1time = timetoampm(times.child("0").child("1").value.toString())
            val period2time = timetoampm(times.child("1").child("1").value.toString())
            val period3time = timetoampm(times.child("2").child("1").value.toString())
            val period4time = timetoampm(times.child("3").child("1").value.toString())
            val period5time = timetoampm(times.child("4").child("1").value.toString())
            val period6time = timetoampm(times.child("5").child("1").value.toString())


            val timeob = DayPeriod(temp, period1time, period2time, period3time, period4time, period5time, period6time)
            DayList.add(timeob)


            for (day in days){


                val currday = day.child(list[i]).key.toString()
                val period1 = day.child(list[i]).child("0").child("2").child("Subject").value.toString()
                val period2 = day.child(list[i]).child("1").child("2").child("Subject").value.toString()
                val period3 = day.child(list[i]).child("2").child("2").child("Subject").value.toString()
                val period4 = day.child(list[i]).child("3").child("2").child("Subject").value.toString()
                val period5 = day.child(list[i]).child("4").child("2").child("Subject").value.toString()
                val period6 = day.child(list[i]).child("5").child("2").child("Subject").value.toString()
                val period7 = day.child(list[i]).child("6").child("2").child("Subject").value.toString()

                i += 1





                val dayperiodob = DayPeriod(currday, period1, period2, period3, period4, period5, period6, period7)

                DayList.add(dayperiodob)



            }

            recyclerview.adapter = TTAdapter(DayList)
        }.addOnFailureListener{
            Toast.makeText(this, "Failed to display", Toast.LENGTH_SHORT).show()
        }
    }


    private fun refreshapp() {

        binding.refresh.setOnRefreshListener {
            Toast.makeText(this, "Refreshed", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, FullTTActivity::class.java)

            startActivity(intent)
            binding.refresh.isRefreshing = false
            finish()
        }
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


    private fun loaddata(): Users {
        val sharedPreferences = getSharedPreferences("sharedprefs", MODE_PRIVATE)
        val name = sharedPreferences.getString("name", null)
        val username = sharedPreferences.getString("username", null)
        val urn = sharedPreferences.getString("urn", null)
        val rollno = sharedPreferences.getString("rollno", null)
        val section = sharedPreferences.getString("section", null)
        val semester = sharedPreferences.getString("semester", null)
        val email = sharedPreferences.getString("email", null)

        val user = Users(name, username, urn, semester, rollno, section, email)

        return user

//        Toast.makeText(this, "saved string $savedstring", Toast.LENGTH_SHORT).show()
    }


    private fun displayfullTT() {
        val intent = Intent(this, FullTTActivity::class.java)
        startActivity(intent)
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


    private fun displayNavbar() {
        val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        toggle1 = ActionBarDrawerToggle(this, drawerLayout, R.string.menu_drawer_open, R.string.menu_drawer_close)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        drawerLayout.addDrawerListener(toggle1)
        toggle1.syncState()
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
        }
    }


}