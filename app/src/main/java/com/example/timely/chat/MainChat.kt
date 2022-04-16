package com.example.timely.chat

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timely.adapter.chat_user_adapter
import com.example.timely.dataClasses.User
import com.example.timely.databinding.MainchatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainChat: AppCompatActivity() {
    private lateinit var chatUserRecyclerView: RecyclerView
    private lateinit var chatuserList:ArrayList<User>
    private lateinit var chatUserAdapter: chat_user_adapter
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var binding: MainchatBinding

    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        binding= MainchatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // variables needed to initialise recycler view
        auth = FirebaseAuth.getInstance()
        database=FirebaseDatabase.getInstance("https://timely-524da-default-rtdb.asia-southeast1.firebasedatabase.app/").reference

        chatuserList= ArrayList()
        chatUserAdapter= chat_user_adapter(this,chatuserList)

        chatUserRecyclerView=binding.chatRecyclerview

        chatUserRecyclerView.layoutManager=LinearLayoutManager(this)
        chatUserRecyclerView.adapter=chatUserAdapter

        database.child("Users").addValueEventListener(object: ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {

                chatuserList.clear()// har bhar data lata h isliye
                for(postsnapshot in snapshot.children){
                    // firebase se data lane ka
                    val currentUser= postsnapshot.getValue(User::class.java)


                        chatuserList.add(currentUser!!)


                }
                chatUserAdapter.notifyDataSetChanged()  // jo jo data change hua h
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}