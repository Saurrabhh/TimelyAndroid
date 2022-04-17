package com.example.timely.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.timely.R
import com.example.timely.dataClasses.Student

class AttendanceAdapter(val context: Context, private val StudentList: ArrayList<Student>): RecyclerView.Adapter<AttendanceAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_attendance, parent, false)

        return MyViewHolder(view)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val checkallp = context.getSharedPreferences("checkall", AppCompatActivity.MODE_PRIVATE)
        val checkall = checkallp.getBoolean("all", false)
        val currentItem = StudentList[position]
        holder.name.text = currentItem.name
        holder.roll.text = currentItem.rollno
        if (currentItem.present == true){
            holder.radioGroup.check(R.id.Present)
        }else if (currentItem.present == false){
            holder.radioGroup.check(R.id.Absent)
        }else{
            holder.radioGroup.clearCheck()
        }
        holder.present.setOnClickListener{
            currentItem.present = true
        }
        holder.absent.setOnClickListener{
            currentItem.present = false
        }

        if (checkall){
            holder.radioGroup.check(R.id.Present)

        }else{
            holder.radioGroup.check(R.id.Absent)

        }
    }

    override fun getItemCount(): Int {
       return StudentList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.Att_Student)
        val roll: TextView = itemView.findViewById(R.id.Att_Rollno)
        val present: RadioButton = itemView.findViewById(R.id.Present)
        val absent: RadioButton = itemView.findViewById(R.id.Absent)
        val radioGroup: RadioGroup = itemView.findViewById(R.id.radiogrp)

    }

}


