package com.example.timely.fragments

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import com.example.timely.databinding.FragmentProfileBinding
import com.example.timely.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
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
    ): View {
        // Inflate the layout for this fragment
        auth = FirebaseAuth.getInstance()


        binding =  FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    //for motion layout swipe to edit function


        motionLayout = binding.motionlayout
        val mainname = binding.MainName
        val mainusername = binding.MainUserName
        val mainurn = binding.MainURN
        val mainclass = binding.MainClass
        val mainsection = binding.MainSection
        val mainsemester = binding.MainSemester
        val mainemail = binding.MainEmail
        val mainbranch = binding.MainBranch
        val maingender = binding.MainGender
        val mainphone = binding.MainPhone
        val mainenroll = binding.MainEnroll


        onEdit = false //flag variable
        mainusername.inputType = InputType.TYPE_NULL
        mainname.inputType = InputType.TYPE_NULL
        mainurn.inputType = InputType.TYPE_NULL
        mainclass.inputType = InputType.TYPE_NULL
        mainsection.inputType = InputType.TYPE_NULL
        mainsemester.inputType = InputType.TYPE_NULL
        mainemail.inputType = InputType.TYPE_NULL
        mainbranch.inputType = InputType.TYPE_NULL
        maingender.inputType = InputType.TYPE_NULL
        mainphone.inputType = InputType.TYPE_NULL
        mainenroll.inputType = InputType.TYPE_NULL


        KEY.fragmentName = KEY().PROFILE

        val user = Utils().loaddata(requireActivity())

        val name = user.name
        val username = user.username
        val urn = user.urn
        val rollno = user.rollno
        val section = user.section
        val semester = user.semester
        val email = user.email
        val branch = user.branch
        val gender = user.gender
        val phoneno = user.phoneno
        val enroll = user.enroll


        binding.MainName.setText(name)
        binding.MainUserName.setText(username)
        binding.MainURN.setText(urn)
        binding.MainClass.setText(rollno)
        binding.MainSection.setText(section)
        binding.MainSemester.setText(semester)
        binding.MainEmail.setText(email)
        binding.MainBranch.setText(branch)
        binding.MainGender.setText(gender)
        binding.MainPhone.setText(phoneno)
        binding.MainEnroll.setText(enroll)



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
                        mainphone.inputType = InputType.TYPE_CLASS_TEXT
                        mainenroll.inputType = InputType.TYPE_CLASS_TEXT

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
                        mainphone.inputType = InputType.TYPE_NULL
                        mainenroll.inputType = InputType.TYPE_NULL

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


