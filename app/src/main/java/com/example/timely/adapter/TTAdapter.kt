package com.example.timely.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.timely.dataClasses.DayPeriod
import com.example.timely.R

class TTAdapter (private val DayList: ArrayList<DayPeriod>) : RecyclerView.Adapter<TTAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.full_tt_view_design, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val currentItem = DayList[position]

        holder.period2.text = currentItem.period2
        holder.period3.text = currentItem.period3
        holder.period4.text = currentItem.period4
        holder.period5.text = currentItem.period5
        holder.period6.text = currentItem.period6


        if (currentItem.currday!="null")
            holder.day.text = currentItem.currday
        else
            holder.day.text = ""

        if (currentItem.period1!="null")
            holder.period1.text = currentItem.period1
        else
            holder.period1.text = ""


        if (currentItem.period2!="null")
            holder.period2.text = currentItem.period2
        else
            holder.period2.text = ""


        if (currentItem.period3!="null")
            holder.period3.text = currentItem.period3
        else
            holder.period3.text = ""


        if (currentItem.period4!="null")
            holder.period4.text = currentItem.period4
        else
            holder.period4.text = ""


        if (currentItem.period5!="null")
            holder.period5.text = currentItem.period5
        else
            holder.period5.text = ""


        if (currentItem.period6!="null")
            holder.period6.text = currentItem.period6
        else
            holder.period6.text = ""

        if (currentItem.period7!="null")
            holder.period7.text = currentItem.period7
        else
            holder.period7.text = ""





    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return DayList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val day: TextView = itemView.findViewById(R.id.Main_Day)
        val period1: TextView = itemView.findViewById(R.id.Main_period1)
        val period2: TextView = itemView.findViewById(R.id.Main_period2)
        val period3: TextView = itemView.findViewById(R.id.Main_period3)
        val period4: TextView = itemView.findViewById(R.id.Main_period4)
        val period5: TextView = itemView.findViewById(R.id.Main_period5)
        val period6: TextView = itemView.findViewById(R.id.Main_period6)
        val period7: TextView = itemView.findViewById(R.id.Main_period7)


    }
}