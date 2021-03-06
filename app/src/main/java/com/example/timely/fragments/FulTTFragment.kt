package com.example.timely.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timely.R
import com.example.timely.adapter.TTAdapter
import com.example.timely.dataClasses.DayPeriod
import com.example.timely.dataClasses.User
import com.example.timely.databinding.FragmentFulTTBinding
import com.example.timely.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class FulTTFragment : Fragment() {

    private lateinit var binding: FragmentFulTTBinding
    private lateinit var DayList: ArrayList<DayPeriod>
    private lateinit var database: DatabaseReference
    private lateinit var recyclerview: RecyclerView
    private lateinit var toggle1 : ActionBarDrawerToggle
    private lateinit var auth : FirebaseAuth
    private lateinit var toggle : ActionBarDrawerToggle
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        auth = FirebaseAuth.getInstance()
        binding = FragmentFulTTBinding.inflate(layoutInflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        KEY.fragmentName = KEY().FULLTT
        navController = Navigation.findNavController(view)
        refreshapp()





        // getting the recyclerview by its id
        recyclerview = binding.fullTTRecycler

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(activity)

        // ArrayList of class ItemsViewModel
        DayList = arrayListOf()

        displayfulltt()
    }

    private fun displayfulltt() {

        val user = Utils.loaddata(requireActivity())

        database = FirebaseDatabase.getInstance("https://timely-524da-default-rtdb.asia-southeast1.firebasedatabase.app/").reference
        var root = database
        var root2 = database
        if (user.isteacher){
            root = database.child("TeacherTT")
            root2 = auth.uid?.let { root.child(it) }!!
        }else{
            root = database.child("Timetable")
            root2 = root.child(user.branch!!).child("Sem ${user.semester}").child("${user.section}")

        }
        root2.get().addOnSuccessListener {
            if (it.exists()){
                val days = it.children
                val list = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
                var i = 0

                val times = it.child("0").child("Monday")

                val temp = "Time"



                val period1time = times.child("0").child("1").value.toString()
                val period2time = times.child("1").child("1").value.toString()
                val period3time = times.child("2").child("1").value.toString()
                val period4time = times.child("3").child("1").value.toString()
                val period5time = times.child("4").child("1").value.toString()
                val period6time = times.child("5").child("1").value.toString()
                val period7time = times.child("6").child("1").value.toString()


                val timeob = DayPeriod(temp, period1time, period2time, period3time, period4time, period5time, period6time, period7time)
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
            }
            else
                Toast.makeText(activity, "TimeTable of this class is not available. It will available soon", Toast.LENGTH_LONG).show()
        }.addOnFailureListener{
            Toast.makeText(activity, "Failed to fetch data. Check your connection", Toast.LENGTH_SHORT).show()
        }
    }


    private fun refreshapp() {

        binding.refresh.setOnRefreshListener {
            Toast.makeText(activity, "Refreshed", Toast.LENGTH_SHORT).show()
//            val ft: FragmentTransaction = requireFragmentManager().beginTransaction()
//            if (Build.VERSION.SDK_INT >= 26) {
//                ft.setReorderingAllowed(false)
//            }
//            ft.detach(this).attach(this).commit()
            navController.navigate(R.id.action_fulTTFragment_self)
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



}