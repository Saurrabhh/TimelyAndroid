package com.example.timely.fragments

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
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
    private lateinit var semester: String
    private lateinit var section: String
    private lateinit var subject: String
    private lateinit var checkall: SharedPreferences
    private lateinit var fileName: String



    private lateinit var filePath: File


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAttendanceBinding.inflate(layoutInflater, container, false)
        auth = FirebaseAuth.getInstance()

        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        KEY.fragmentName = KEY().ATTENDANCE
        val currentdate = LocalDateTime.now()
        val dateformatter = DateTimeFormatter.ofPattern("dd MMM yyyy")

        database = FirebaseDatabase.getInstance("https://timely-524da-default-rtdb.asia-southeast1.firebasedatabase.app/").reference
        date = currentdate.format(dateformatter)

        val sharedPreferences = requireActivity().getSharedPreferences("currperiod", AppCompatActivity.MODE_PRIVATE)
        time = sharedPreferences.getString("time", null).toString()
        subject = sharedPreferences.getString("subject", null).toString()
        semester = sharedPreferences.getString("semester", null).toString()
        section = sharedPreferences.getString("section", null).toString()


        studentList = arrayListOf()
        showStudent()

        recyclerview = binding.AttedanceRecyclerview
        adapter = AttendanceAdapter(requireContext(), studentList)
        recyclerview.layoutManager = LinearLayoutManager(activity)
        recyclerview.adapter = adapter




        binding.MainAttTime.text = time
        binding.MainAttDate.text = date
        binding.MainAttSec.text = section
        binding.MainAttSem.text = semester


        checkall = requireActivity().getSharedPreferences("checkall", AppCompatActivity.MODE_PRIVATE)
        val editorall = checkall.edit()


        binding.AllPresent.setOnClickListener{

            if (binding.AllPresent.isChecked){
                editorall.apply {
                    putBoolean("allpresent", true)
                }.apply()
            }else{
                editorall.apply{
                    putBoolean("allpresent", false)
                }
            }
            adapter.notifyDataSetChanged()
        }

        binding.AllAbsent.setOnClickListener{

            if (binding.AllAbsent.isChecked){
                editorall.apply {
                    putBoolean("allabsent", true)
                }.apply()
            }else{
                editorall.apply{
                    putBoolean("allabsent", false)
                }
            }
            adapter.notifyDataSetChanged()
        }

        fileName = "Demo.xls"

        val origin = requireContext().getExternalFilesDir(null)
        val origin1 = origin.toString().slice(1..18)
        val file = File(origin, fileName)
        var fileInputStream: FileInputStream? = null
        var fileOutputStream: FileOutputStream? = null





        Log.d("exx", origin.toString().slice(1..18))
        Log.d("excel", file.toString())


        binding.saveButton.setOnClickListener {

            var done = false

            for (student in studentList){
                if (student.present == null){
                    Toast.makeText(requireContext(), "Fill attendance of every student.", Toast.LENGTH_SHORT).show()
                    done = false
                    break
                }
                done = true
            }

            if (done){
                database = FirebaseDatabase.getInstance("https://timely-524da-default-rtdb.asia-southeast1.firebasedatabase.app/").reference
                Log.d("hello", studentList.toString())
                database.child("Attendance").child(subject+time+date).setValue(studentList).addOnSuccessListener {
                    Toast.makeText(requireContext(), "Attendance Saved", Toast.LENGTH_SHORT).show()
                }
                var i = 1
                val hssfWorkbook = HSSFWorkbook()
                val hssfSheet = hssfWorkbook.createSheet("Attendance Sheet")
                hssfSheet.setColumnWidth(0, (10))
                hssfSheet.setColumnWidth(1, (15 * 500))
                hssfSheet.setColumnWidth(2, (15 * 20))
                val headerrow = hssfSheet.createRow(0)
                val headerroll = headerrow.createCell(0)
                val headername = headerrow.createCell(1)
                val headerpresent = headerrow.createCell(2)

                headerroll.setCellValue("Roll No.")
                headername.setCellValue("Name")
                headerpresent.setCellValue("Attendance")



                for (student in studentList) {


                    val hssfRow = hssfSheet.createRow(i)
                    val rollno = hssfRow.createCell(0)
                    val name = hssfRow.createCell(1)

                    val present = hssfRow.createCell(2)
                    rollno.setCellValue(student.rollno)
                    name.setCellValue(student.name)
                    val p = student.present
                    val a: String = if (p == true) {
                        "Present"
                    } else {
                        "Absent"
                    }
                    present.setCellValue(a)
                    i += 1
                        }
                try {

                        fileOutputStream = FileOutputStream(file)

                        hssfWorkbook.write(fileOutputStream)

                    }catch (e: Exception){
                        e.printStackTrace()
                    }finally {
                        fileOutputStream!!.close()
                    }

                }
            }



    }

    private fun showStudent() {
        database = FirebaseDatabase.getInstance("https://timely-524da-default-rtdb.asia-southeast1.firebasedatabase.app/").reference

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