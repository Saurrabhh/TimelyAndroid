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
import kotlin.properties.Delegates

class ProfileFragment : Fragment() {

    lateinit var binding: FragmentProfileBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var motionLayout: MotionLayout
    private var onEdit by Delegates.notNull<Boolean>()

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
    //for motion layout swipe to edit function


        motionLayout = binding.motionlayout
        val mainname=binding.MainName
        val mainusername = binding.MainUserName
        val mainurn=binding.MainURN
        val mainclass=binding.MainClass
        val mainsection=binding.MainSection
        val mainsemester=binding.MainSemester
        val mainemail=binding.MainEmail
        val mainbranch=binding.MainBranch
        val maingender=binding.MainGender


        onEdit=false //flag variable
        mainusername.inputType = InputType.TYPE_NULL
        mainname.inputType = InputType.TYPE_NULL
        mainurn.inputType = InputType.TYPE_NULL
        mainclass.inputType = InputType.TYPE_NULL
        mainsection.inputType = InputType.TYPE_NULL
        mainsemester.inputType = InputType.TYPE_NULL
        mainemail.inputType = InputType.TYPE_NULL
        mainbranch.inputType = InputType.TYPE_NULL
        maingender.inputType = InputType.TYPE_NULL



        KEY.fragmentName = KEY().PROFILE
        database = FirebaseDatabase.getInstance("https://timely-524da-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users")
        database.get().addOnSuccessListener {

            val currentemail = auth.currentUser?.email.toString()
            val users = it.children
            for (user in users){
                if ( user.child("email").value.toString() == currentemail ){

                    binding.MainName.setText(user.child("name").value.toString())
                    binding.MainUserName.setText(user.child("username").value.toString())
                    binding.MainURN.setText(user.child("urn").value.toString())
                    binding.MainClass.setText(user.child("rollno").value.toString())
                    binding.MainSection.setText(user.child("section").value.toString())
                    binding.MainSemester.setText(user.child("semester").value.toString())
                    binding.MainEmail.setText(user.child("email").value.toString())
                    binding.MainBranch.setText(user.child("branch").value.toString())
                    binding.MainGender.setText(user.child("gender").value.toString())
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
                //Toast.makeText(activity, currentId.toString(), Toast.LENGTH_SHORT).show()
                if (motionLayout != null) {
                    if (!onEdit){
                        mainusername.inputType = InputType.TYPE_CLASS_TEXT
                        mainname.inputType = InputType.TYPE_CLASS_TEXT
                        mainurn.inputType = InputType.TYPE_CLASS_TEXT
                        mainclass.inputType = InputType.TYPE_CLASS_TEXT
                        mainsection.inputType = InputType.TYPE_CLASS_TEXT
                        mainsemester.inputType = InputType.TYPE_CLASS_TEXT
                        mainemail.inputType = InputType.TYPE_CLASS_TEXT
                        mainbranch.inputType = InputType.TYPE_CLASS_TEXT
                        maingender.inputType = InputType.TYPE_CLASS_TEXT

                        //Toast.makeText(activity, currentId.toString(), Toast.LENGTH_SHORT).show()
                        onEdit=true

                    } else{
                        mainusername.inputType = InputType.TYPE_NULL
                        mainname.inputType = InputType.TYPE_NULL
                        mainurn.inputType = InputType.TYPE_NULL
                        mainclass.inputType = InputType.TYPE_NULL
                        mainsection.inputType = InputType.TYPE_NULL
                        mainsemester.inputType = InputType.TYPE_NULL
                        mainemail.inputType = InputType.TYPE_NULL
                        mainbranch.inputType = InputType.TYPE_NULL
                        maingender.inputType = InputType.TYPE_NULL

                        onEdit=false

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