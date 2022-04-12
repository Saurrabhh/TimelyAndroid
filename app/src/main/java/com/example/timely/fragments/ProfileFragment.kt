package com.example.timely.fragments

import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.navigation.Navigation
import com.example.timely.R
import com.example.timely.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    lateinit var binding: FragmentProfileBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var motionLayout: MotionLayout

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

        motionLayout = binding.motionlayout
        val mainusername = binding.MainUserName
        mainusername.inputType = InputType.TYPE_NULL

        KEY.fragmentName = KEY().PROFILE
        database = FirebaseDatabase.getInstance("https://timely-524da-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users")
        database.get().addOnSuccessListener {

            val currentemail = auth.currentUser?.email.toString()
            val users = it.children
            for (user in users){
                if ( user.child("email").value.toString() == currentemail ){

                    binding.MainName.text = user.child("name").value.toString()
                    binding.MainUserName.setText(user.child("username").value.toString())
                    binding.MainURN.text = user.child("urn").value.toString()
                    binding.MainClass.text = user.child("rollno").value.toString()
                    binding.MainSection.text = user.child("section").value.toString()
                    binding.MainSemester.text = user.child("semester").value.toString()
                    binding.MainEmail.text = user.child("email").value.toString()
                    binding.MainBranch.text = user.child("branch").value.toString()
                    binding.MainGender.text = user.child("gender").value.toString()
                    break
                }
            }
        }.addOnFailureListener{
            Toast.makeText(activity, "Failed to fetch data. Check your connection", Toast.LENGTH_SHORT).show()
        }


        motionLayout.addTransitionListener(object: MotionLayout.TransitionListener{
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {
                Log.d("Started","Started")
            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
                Log.d("Change","Change")
            }



            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {

                if (motionLayout != null) {
                    if (currentId == 2131362137){
                        mainusername.inputType = InputType.TYPE_CLASS_TEXT
                        Toast.makeText(activity, currentId.toString(), Toast.LENGTH_SHORT).show()
                    }else{
                        mainusername.inputType = InputType.TYPE_NULL
                    }
                }
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {
                Log.d("Trigger","Trigger")
            }

        })




    }
}