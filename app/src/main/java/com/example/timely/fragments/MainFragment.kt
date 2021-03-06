package com.example.timely.fragments

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timely.R
import com.example.timely.activity.LoginActivity
import com.example.timely.activity.MainActivity
import com.example.timely.adapter.MyAdapter
import com.example.timely.dataClasses.Periods
import com.example.timely.dataClasses.User
import com.example.timely.databinding.FragmentMainBinding
import com.example.timely.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson


import java.text.SimpleDateFormat
import java.util.*


class MainFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentMainBinding
    private lateinit var day: String
    lateinit var notificationManager: NotificationManager
    private lateinit var notificationChannel: NotificationChannel
    private lateinit var builder: Notification.Builder
    private val channelId = "i.apps.notifications"
    private val description = "Test notification"
    private lateinit var database: DatabaseReference
    private lateinit var recyclerview: RecyclerView
    private lateinit var PeriodList: ArrayList<Periods>
    private lateinit var navController: NavController
    private lateinit var user: User
    private lateinit var progressBar: ProgressBar



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        PeriodList = arrayListOf()

        auth = FirebaseAuth.getInstance()

        progressBar = binding.progressBar

        user = Utils.loaddata(requireActivity())
        binding.NameTop.text = user.name
        displayPeriodData()
        return binding.root

        //for progress bar

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        KEY.fragmentName = KEY().HOME



        if(auth.currentUser == null){
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }




        navController = Navigation.findNavController(view)
        refreshapp()


        recyclerview = binding.recyclerview

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(activity)

//        recyclerview.setHasFixedSize(true)

        // ArrayList of class ItemsViewModel


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
            Toast.makeText(activity, "Refreshed", Toast.LENGTH_SHORT).show()
//            val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
//
//            ft.detach(this).attach(this).commit()

            try {
                navController.navigate(R.id.action_mainFragment_self)
            }catch (e: Exception){
                navController.navigate(R.id.action_teacherFragment_self)
            }
        }
    }

    private fun displayPeriodData() {

        var nextclassflag = 0

        database = FirebaseDatabase.getInstance("https://timely-524da-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Timetable")
        database.child("${user.branch}").child("Sem ${user.semester}").child("${user.section}").get().addOnSuccessListener {


                val data = getcurrentday()


                val periods = it.child(data[1]).child(data[0]).children
                var newtimeleft = 0

                for (period in periods){
                    val periodno = period.child("0").value.toString()
                    var time = period.child("1").value.toString()
                    val subject = period.child("2").child("Subject").value.toString()
                    val teacher = period.child("2").child("Teacher").value.toString()


                    val array = time.split("-")
                    val st = convertTomili(array[0])
                    val et = convertTomili(array[1])
                    val currtime = convertTomili(getcurrenttime())

                    if (nextclassflag==1){
                        binding.MainNextPeriod.text = subject
                        binding.NextProfName.text=teacher
                        if(!user.isteacher)
                            createNotification(subject, newtimeleft.toString())
                        nextclassflag = 0
                    }

                    if(currtime in st..et) {
                        Log.d("et", et.toString())
                        Log.d("ct", currtime.toString())
                        var timeleft = (et - currtime).toString()

                        val sharedPreferences1 =
                            activity?.getSharedPreferences("currperiod", AppCompatActivity.MODE_PRIVATE)
                        val editor1 = sharedPreferences1?.edit()

                        editor1?.apply {
                            putString("time", time)
                            putString("subject", subject)
                        }?.apply()



                        timeleft = (timeleft.toInt() - 40).toString()


                        newtimeleft = timeleft.toInt()
                        timeleft = "$timeleft mins"
                        binding.CurrProfName.text=teacher
                        binding.MainTime.text = timeleft

                        //for progress bar

                        Thread(Runnable {
                            var count = newtimeleft * 100
                            while (count > 0){
                                count -= 1
                                progressBar.progress = count
                                Thread.sleep(1000)
                            }
                        }).start()

                        binding.MainCurrentClass.text = subject
                        nextclassflag = 1

                    }

//                    val mili = requireActivity().intent.getStringExtra("gomili").toString()
////                Toast.makeText(this, time.toString(), Toast.LENGTH_SHORT).show()
//                    if (mili != "yes"){
//                        time = timetoampm(time)
//                    }

                    val periodobject = Periods(periodno, time, subject, teacher)

                    PeriodList.add(periodobject)

                }

                recyclerview.adapter = MyAdapter(PeriodList)
            }
        .addOnFailureListener{
            Toast.makeText(requireContext(), "Failed to fetch data. Check your connection", Toast.LENGTH_SHORT).show()
        }

    }

    private fun convertTomili(time: String): Int {
        val arr = time.split(":")
        return (arr[0] + arr[1]).toInt()
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
            newstarttime = if (newstarttime.toInt()<9){
                "0"+newstarttime+":"+starttarr[1]+ "pm"
            } else{
                newstarttime+":"+starttarr[1]+ "pm"
            }

        }
        else{
            newstarttime = starttarr[0]

            newstarttime = if (newstarttime.toInt()<9){
                "0"+newstarttime+":"+starttarr[1]+ "am"
            } else{
                newstarttime+":"+starttarr[1]+ "am"
            }
        }

        if (endtarr[0].toInt() > 12){
            newendtime = (endtarr[0].toInt() - 12).toString()
            newendtime = if (newendtime.toInt()<9){
                "0"+newendtime+":"+endtarr[1]+ "pm"
            } else{
                newendtime+":"+endtarr[1]+ "pm"
            }
        }
        else{
            newendtime = endtarr[0]
            newendtime = if (newendtime.toInt()<9){
                "0"+newendtime+":"+endtarr[1]+ "am"
            } else{
                newendtime+":"+endtarr[1]+ "am"
            }
        }



        return "$newstarttime-$newendtime"

    }



    private fun createNotification(nextPeriod: String, timeLeft: String){


//        val nextPeriod: String = getcurrenttime()
//        val timeLeft: String = getcurrenttime()


         notificationManager =activity?.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager

        //After Clicking notification come to this activity
        val intent = Intent(activity, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)


        notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.GREEN
        notificationChannel.enableVibration(false)
        notificationManager.createNotificationChannel(notificationChannel)

        builder = Notification.Builder(activity, channelId)
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
        notificationManager.notify(1234, builder.build())

    }



}