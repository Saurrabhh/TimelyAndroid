package com.example.timely.fragments

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import com.example.timely.dataClasses.User
import com.example.timely.databinding.FragmentProfileBinding
import com.example.timely.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlin.properties.Delegates

class ProfileFragment : Fragment() {

    lateinit var binding: FragmentProfileBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var motionLayout: MotionLayout
    private lateinit var name: String
    private lateinit var username: String
    private lateinit var urn: String
    private lateinit var rollno: String
    private lateinit var section: String
    private lateinit var semester: String
    private lateinit var email: String
    private lateinit var branch: String
    private lateinit var gender: String
    private lateinit var phoneno: String
    private lateinit var enroll: String

    private var onEdit by Delegates.notNull<Boolean>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance("https://timely-524da-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users")


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

        val user = Utils.loaddata(requireActivity())

        name = user.name.toString()
        username = user.username.toString()
        urn = user.urn.toString()
        rollno = user.rollno.toString()
        section = user.section.toString()
        semester = user.semester.toString()
        email = user.email.toString()
        branch = user.branch.toString()
        gender = user.gender.toString()
        phoneno = user.phoneno.toString()
        enroll = user.enroll.toString()


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
                        onEdit = true

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

                        val name = binding.MainName.text.toString()
                        val username = binding.MainUserName.text.toString()
                        val urn = binding.MainURN.text.toString()
                        val rollno = binding.MainClass.text.toString()
                        val section = binding.MainSection.text.toString()
                        val semester = binding.MainSemester.text.toString()
                        val email = binding.MainEmail.text.toString()
                        val branch = binding.MainBranch.text.toString()
                        val gender = binding.MainGender.text.toString()
                        val phoneno = binding.MainPhone.text.toString()
                        val enroll = binding.MainEnroll.text.toString()

                        val user = User(auth.uid, name, username, urn, semester, rollno, section, email, gender, branch, phoneno, enroll)

                        database.child(auth.uid!!).setValue(user)
//                        Toast.makeText(requireContext(), user.toString(), Toast.LENGTH_SHORT).show()
                        Toast.makeText(requireContext(), "saved", Toast.LENGTH_SHORT).show()

                        onEdit = false

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


