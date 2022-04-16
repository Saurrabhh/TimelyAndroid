package com.example.timely.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.timely.R
import com.example.timely.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class ChatActivity : AppCompatActivity() {
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: ImageButton
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList:ArrayList<Message>
    private lateinit var database: DatabaseReference
    private lateinit var binding: ActivityChatBinding

    var receiverRoom:String? = null
    var senderRoom:String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")// yaha change karna padega baad m
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid

        database = FirebaseDatabase.getInstance("https://timely-524da-default-rtdb.asia-southeast1.firebasedatabase.app/").reference

        senderRoom = receiverUid+senderUid
        receiverRoom = senderUid+receiverUid
        supportActionBar?.title = name


        chatRecyclerView = binding.displayChatRecycler
        messageBox = binding.messageBox
        sendButton = binding.sendButton
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)

        sendButton.setOnClickListener{

            val message = messageBox.text.toString()
            val messageObject = Message(message, senderUid)

            database.child("Chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    database.child("Chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }

        }
    }
}