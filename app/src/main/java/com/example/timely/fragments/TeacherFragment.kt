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
import com.example.timely.utils.Utils
import com.example.timely.databinding.FragmentTeacherBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*


open class TeacherFragment : Fragment() {


    private lateinit var auth: FirebaseAuth
    private lateinit var user: User

    private lateinit var binding: FragmentTeacherBinding
    private lateinit var day: String
    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationChannel: NotificationChannel
    private lateinit var builder: Notification.Builder
    private val channelId = "i.apps.notifications"
    private val description = "Test notification"
    private lateinit var database: DatabaseReference
    private lateinit var recyclerview: RecyclerView
    private lateinit var PeriodList: ArrayList<Periods>
    private lateinit var navController: NavController
    private lateinit var progressbar_tc: ProgressBar


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTeacherBinding.inflate(layoutInflater, container, false)
        PeriodList = arrayListOf()
//        Toast.makeText(activity, "teacher", Toast.LENGTH_SHORT).show()

        auth = FirebaseAuth.getInstance()
        user = Utils.loaddata(requireActivity())
        progressbar_tc = binding.progressBarTc
        displayPeriodData()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        KEY.fragmentName = KEY().TEACHER

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

            navController.navigate(R.id.action_teacherFragment_self)
        }
    }

    private fun displayPeriodData() {

        binding.NameTop.text = user.name
        var nextclassflag = 0

//        Toast.makeText(this, d.toString(), Toast.LENGTH_SHORT).show()
        database = FirebaseDatabase.getInstance("https://timely-524da-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("TeacherTT")
        database.child(auth.uid!!).get().addOnSuccessListener {

            if (it.exists()){
                val data = getcurrentday()


                val periods = it.child(data[1]).child(data[0]).children
                Log.d("dinesh", periods.toString())
                var newtimeleft = 0

                for (period in periods){
                    val periodno = period.child("0").value.toString()
                    var time = period.child("1").value.toString()
                    val subject = period.child("2").child("Subject").value.toString()
                    val semester = period.child("2").child("Semester").value.toString()
                    val section = period.child("2").child("Section").value.toString()


                    val array = time.split("-")
                    val st = convertTomili(array[0])
                    val et = convertTomili(array[1])
                    val currtime = convertTomili(getcurrenttime())

                    if (nextclassflag==1){
                        binding.MainNextPeriod.text = subject
                        binding.NextMainSection.text = section
                        binding.NextMainSemester.text = semester
                        nextclassflag = 0
                    }

                    if(currtime in st..et) {
                        val sharedPreferences = requireActivity().getSharedPreferences("currperiod", AppCompatActivity.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.apply {
                            putString("time", time)
                            putString("subject", subject)
                            putString("semester", semester)
                            putString("section", section)
                        }.apply()
                        var timeleft = (et - currtime).toString()

                        if (timeleft.toInt()>=60){
                            timeleft = (timeleft.toInt() - 40).toString()
                        }

                        newtimeleft = timeleft.toInt()
                        timeleft = "$timeleft mins"

                        binding.MainTime.text = timeleft
                        //for progress bar

                        Thread(Runnable {
                            var count = newtimeleft * 100
                            while (count > 0){
                                count -= 1
                                progressbar_tc.progress = count
                                Thread.sleep(1000)
                            }
                        }).start()

                        binding.MainCurrentClass.text = subject
                        binding.MainSection.text = section
                        binding.MainSemester.text = semester
                        nextclassflag = 1
                    }

                    val mili = requireActivity().intent.getStringExtra("gomili").toString()
//                Toast.makeText(this, time.toString(), Toast.LENGTH_SHORT).show()
                    if (mili != "yes"){
                        time = timetoampm(time)
                    }


//                Toast.makeText(this, mili, Toast.LENGTH_SHORT).show()

                    val teacher = period.child("2").child("Teacher").value.toString()

                    val periodobject = Periods(periodno, time, subject, teacher)

                    PeriodList.add(periodobject)

                }

                recyclerview.adapter = MyAdapter(PeriodList)
            }
            else
                Toast.makeText(activity, "TimeTable is only availabe for Sem3 E/F and sem 5 A/B", Toast.LENGTH_SHORT).show()

        }.addOnFailureListener{
            Toast.makeText(activity, "Failed to fetch data. Check your connection", Toast.LENGTH_SHORT).show()
        }

    }



    private fun getcurrentuserdata(){

        val currentemail = auth.currentUser?.email.toString()

        database = FirebaseDatabase.getInstance("https://timely-524da-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users")
        database.get().addOnSuccessListener {

            val users = it.children
            for (user in users) {
                if (user.child("email").value.toString() == currentemail) {

                    val sharedPreferences = requireActivity().getSharedPreferences("sharedprefs",
                        AppCompatActivity.MODE_PRIVATE
                    )

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

        }.addOnFailureListener{
            Toast.makeText(requireActivity(), "An Error occurred", Toast.LENGTH_SHORT).show()
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


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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
                .setStyle(
                    Notification.InboxStyle()
                    .addLine("Time left: $timeLeft mins")
                    .addLine("Click to see Full Time Table")
                )
                .setContentIntent(pendingIntent)
        }
        else {
            builder = Notification.Builder(activity)
                //.setContent(contentView)
                .setContentTitle("Next Period: $nextPeriod")
                .setContentText("Time Left: $timeLeft mins")
                .setSmallIcon(R.drawable.ic_logo)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.notification_logo))
                .setStyle(
                    Notification.InboxStyle()
                    .addLine("Time left: $timeLeft mins")
                    .addLine("Click to see Full Time Table")
                )
                .setContentIntent(pendingIntent)
        }
        notificationManager.notify(1234, builder.build())

    }
}