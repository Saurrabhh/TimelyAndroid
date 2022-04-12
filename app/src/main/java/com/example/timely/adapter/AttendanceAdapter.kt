package com.example.timely.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.timely.R
import com.example.timely.dataClasses.Students

class AttendanceAdapter(private val StudentList: ArrayList<Students>): RecyclerView.Adapter<AttendanceAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_attendance, parent, false)

        return MyViewHolder(view)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = StudentList[position]
        holder.sname.text = currentItem.sname
        holder.roll.text = currentItem.rollno
    }

    override fun getItemCount(): Int {
       return StudentList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sname: TextView = itemView.findViewById(R.id.Att_Student)
        val roll: TextView = itemView.findViewById(R.id.Att_Rollno)

    }

}


