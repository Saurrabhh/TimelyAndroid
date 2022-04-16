package com.example.timely.chat


import android.content.Context
import android.view.LayoutInflater


import android.view.View
import android.view.ViewGroup

import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.example.timely.R
import com.example.timely.adapter.chat_user_adapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class MessageAdapter(val context: Context, private val messageList:ArrayList<Message>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ITEM_RECEIVE = 1
    private val ITEM_SENT = 2



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if(viewType==1){
            //inlfate receive
            val chatview:View= LayoutInflater.from(context).inflate(R.layout.receive_chat,parent,false)
            return ReceiveViewHolder(chatview)

        }else{
            //inflate sent
            val chatview:View= LayoutInflater.from(context).inflate(R.layout.sent_chat,parent,false)
            return SentViewHolder(chatview)

        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val currentMessage= messageList[position]

        if(holder.javaClass==SentViewHolder::class.java){
            //for sent view holder

            val viewHolder = holder as SentViewHolder
            holder.sentMessage.text= currentMessage.message

        }else{
            //for receive view holder
            val viewHolder = holder as ReceiveViewHolder
            holder.receiveMessage.text=currentMessage.message

        }

    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage= messageList[position]

        if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            return ITEM_SENT
        }else{
            return ITEM_RECEIVE
        }
    }

    override fun getItemCount(): Int {
        return messageList.size

    }

    class SentViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){

        val sentMessage= itemView.findViewById<TextView>(R.id.sent_txt)
    }


    class ReceiveViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){

        val receiveMessage= itemView.findViewById<TextView>(R.id.receive_txt)

    }



}