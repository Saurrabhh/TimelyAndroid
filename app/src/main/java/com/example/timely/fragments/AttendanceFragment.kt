package com.example.timely.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timely.R
import com.example.timely.adapter.AttendanceAdapter
import com.example.timely.dataClasses.Periods
import com.example.timely.dataClasses.Students
import com.example.timely.databinding.FragmentAttendanceBinding
import java.util.ArrayList


class AttendanceFragment : Fragment() {

    private lateinit var recyclerview: RecyclerView
    private lateinit var binding: FragmentAttendanceBinding
    private lateinit var StudentList: ArrayList<Students>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAttendanceBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        KEY.fragmentName = KEY().ATTENDANCE
        StudentList = arrayListOf(Students("Saurabh"), Students("Krish"),Students("Chaitanya"),Students("Shubhankar"))

        recyclerview = binding.AttedanceRecyclerview
        recyclerview.layoutManager = LinearLayoutManager(activity)
        recyclerview.adapter = AttendanceAdapter(StudentList)
    }
}