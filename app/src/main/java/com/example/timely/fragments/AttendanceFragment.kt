package com.example.timely.fragments

import android.os.Bundle
import android.os.Debug
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timely.adapter.AttendanceAdapter
import com.example.timely.dataClasses.Students
import com.example.timely.databinding.FragmentAttendanceBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class AttendanceFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var recyclerview: RecyclerView
    private lateinit var binding: FragmentAttendanceBinding
    private lateinit var StudentList: ArrayList<Students>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAttendanceBinding.inflate(layoutInflater, container, false)
        auth = FirebaseAuth.getInstance()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        KEY.fragmentName = KEY().ATTENDANCE
        StudentList = arrayListOf()

        recyclerview = binding.AttedanceRecyclerview
        recyclerview.layoutManager = LinearLayoutManager(activity)


        showStudent()
    }

    private fun showStudent() {
        database = FirebaseDatabase.getInstance("https://timely-524da-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Students")
        database.get().addOnSuccessListener {
            val students = it.children

            for (student in students){
                val sname = student.child("name").value.toString()
                val rollno = student.child("rollno").value.toString()

                val stdobj = Students(sname, rollno)
                StudentList.add(stdobj)
            }
            recyclerview.adapter = AttendanceAdapter(StudentList)
//           Log.d("HEHE", StudentList.toString())
        }.addOnFailureListener{
            Toast.makeText(activity, "Faliedd", Toast.LENGTH_SHORT).show()
        }
    }
}