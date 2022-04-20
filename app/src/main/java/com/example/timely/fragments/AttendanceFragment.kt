package com.example.timely.fragments

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timely.adapter.AttendanceAdapter
import com.example.timely.dataClasses.Student
import com.example.timely.databinding.FragmentAttendanceBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class AttendanceFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var recyclerview: RecyclerView
    private lateinit var binding: FragmentAttendanceBinding
    private lateinit var studentList: ArrayList<Student>
    private lateinit var adapter: AttendanceAdapter
    private lateinit var time: String
    private lateinit var date: String
    private lateinit var subject: String
    private lateinit var checkall: SharedPreferences



    private lateinit var filePath: File


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAttendanceBinding.inflate(layoutInflater, container, false)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance("https://timely-524da-default-rtdb.asia-southeast1.firebasedatabase.app/").reference
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        KEY.fragmentName = KEY().ATTENDANCE
        val currentdate = LocalDateTime.now()
        val dateformatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
        date = currentdate.format(dateformatter)

        val sharedPreferences = requireActivity().getSharedPreferences("currperiod", AppCompatActivity.MODE_PRIVATE)
        time = sharedPreferences.getString("time", null).toString()
        time = sharedPreferences.getString("subject", null).toString()

        studentList = arrayListOf()
        showStudent()

        recyclerview = binding.AttedanceRecyclerview
        adapter = AttendanceAdapter(requireContext(), studentList)
        recyclerview.layoutManager = LinearLayoutManager(activity)
        recyclerview.adapter = adapter




        binding.MainAttTime.text = time
        binding.MainAttDate.text = date





        checkall = requireActivity().getSharedPreferences("checkall", AppCompatActivity.MODE_PRIVATE)
        val editorall = checkall.edit()


        binding.AllPresent.setOnClickListener{
            Toast.makeText(requireContext(),"all", Toast.LENGTH_SHORT).show()
            if (binding.AllPresent.isChecked){
                editorall.apply {
                    putBoolean("all", true)
                }.apply()
            }else{
                editorall.apply{
                    putBoolean("all", false)
                }
            }
        }

        binding.AllAbsent.setOnClickListener{
            Toast.makeText(requireContext(),"all", Toast.LENGTH_SHORT).show()
            if (binding.AllAbsent.isChecked){
                editorall.apply {
                    putBoolean("all", false)
                }.apply()
            }else{
                editorall.apply{
                    putBoolean("all", true)
                }
            }
        }

        filePath = File( Environment.getExternalStorageDirectory() , "/Demo.txt")


        binding.saveButton.setOnClickListener {

//            try {
//                if (!filePath.exists()){
//                    filePath.createNewFile()
//                }
//                   val fileWriter = FileWriter(filePath)
//                fileWriter.append("Hello")
//
//                    fileWriter.flush()
//                    fileWriter.close()
//                Toast.makeText(requireContext(), "saved", Toast.LENGTH_SHORT).show()
//
//            }catch (e: Exception){
//                e.printStackTrace()
//            }




            var done = false


            for (student in studentList){
                if (student.present == null){
                    Toast.makeText(requireContext(), "sab ka de attendance", Toast.LENGTH_SHORT).show()
                    done = false
                    break
                }
                done = true
            }

            if (done){
                database.child("Attendance").child(subject+time+date).setValue(studentList).addOnSuccessListener {
                    Toast.makeText(requireContext(), "Done attendance", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }

    private fun showStudent() {

        database.child("Students").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (student in snapshot.children){
                    val sname = student.child("name").value.toString()
                    val rollno = student.child("rollno").value.toString()
                    val std = Student(sname, rollno, time, date)
                    studentList.add(std)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }
}