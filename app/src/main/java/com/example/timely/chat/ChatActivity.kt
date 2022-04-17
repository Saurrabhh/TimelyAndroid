package com.example.timely.chat

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timely.adapter.MessageAdapter
import com.example.timely.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

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


    @SuppressLint("SetTextI18n")
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

        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = messageAdapter

        // for adding data into recycler view
        database.child("Chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object: ValueEventListener{
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {

                    messageList.clear()
                    for(postsnapshot in snapshot.children){


                        val message = postsnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        sendButton.setOnClickListener{

            val message = messageBox.text.toString()
            val messageObject = Message(message, senderUid)

            if (message.isNotEmpty()){
                database.child("Chats").child(senderRoom!!).child("messages").push()
                    .setValue(messageObject).addOnSuccessListener {
                        database.child("Chats").child(receiverRoom!!).child("messages").push()
                            .setValue(messageObject)
                    }
            }
            messageBox.setText("")

        }
    }
}