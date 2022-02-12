package com.example.timely.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.timely.R
import com.example.timely.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ProfileFragment : Fragment() {

    lateinit var binding: FragmentProfileBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        auth = FirebaseAuth.getInstance()

        binding =  FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        KEY.fragmentName = KEY().PROFILE
        database = FirebaseDatabase.getInstance("https://timely-524da-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users")
        database.get().addOnSuccessListener {

            val currentemail = auth.currentUser?.email.toString()
            val users = it.children
            for (user in users){
                if ( user.child("email").value.toString() == currentemail ){

                    binding.MainName.text = user.child("name").value.toString()
                    binding.MainUserName.text = user.child("username").value.toString()
                    binding.MainURN.text = user.child("urn").value.toString()
                    binding.MainClass.text = user.child("rollno").value.toString()
                    binding.MainSection.text = user.child("section").value.toString()
                    binding.MainSemester.text = user.child("semester").value.toString()
                    binding.MainEmail.text = user.child("email").value.toString()
                    break
                }
            }
        }.addOnFailureListener{
            Toast.makeText(activity, "Failed to fetch data. Check your connection", Toast.LENGTH_SHORT).show()
        }

        val navController= Navigation.findNavController(view)
        binding.Home.setOnClickListener{
            navController.navigate(R.id.action_profileFragment_to_mainFragment)
        }
    }
}