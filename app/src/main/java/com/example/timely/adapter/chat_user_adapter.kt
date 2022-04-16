package com.example.timely.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.VerifiedInputEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.animation.content.Content
import com.example.timely.R
import com.example.timely.chat.ChatActivity
import com.example.timely.dataClasses.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class chat_user_adapter(val context: Context, val userList:ArrayList<User>): RecyclerView.Adapter<chat_user_adapter.ChatUserViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatUserViewHolder {
        val chatview:View= LayoutInflater.from(context).inflate(R.layout.card_chat_user_design,parent,false)
        return ChatUserViewHolder(chatview)
    }

    override fun onBindViewHolder(holder: ChatUserViewHolder, position: Int) {
        val currentuser= userList[position]

        holder.chatTextName.text=currentuser.name

        holder.itemView.setOnClickListener{
            val intent = Intent(context,ChatActivity::class.java)

            intent.putExtra("name",currentuser.name)
            intent.putExtra("uid", currentuser.uid)

            context.startActivity(intent)

        }

    }

    override fun getItemCount(): Int {
            return userList.size
    }

    class ChatUserViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){

        val chatTextName= itemView.findViewById<TextView>(R.id.chat_name)
    }



}
