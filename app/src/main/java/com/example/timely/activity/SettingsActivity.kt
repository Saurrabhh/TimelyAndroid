package com.example.timely.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.example.timely.R
import com.example.timely.databinding.ActivitySettingsBinding
import com.example.timely.themes.ColorDialogCallback
import com.example.timely.themes.DialogManager
import com.example.timely.themes.ThemeManager
import com.example.timely.themes.ThemeStorage
import com.google.firebase.auth.FirebaseAuth

class SettingsActivity : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var notifSwitch : SwitchCompat
    private lateinit var miliSwitch : SwitchCompat
    lateinit var theme : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        ThemeManager.setCustomizedThemes(this, ThemeStorage.getThemeColor(this))
        setContentView(binding.root)

        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        if (ThemeStorage.getThemeColor(this).equals("blue")) {
            window.statusBarColor = resources.getColor(R.color.colorPrimary)
        }
        auth = FirebaseAuth.getInstance()

        notifSwitch = binding.notificationSwitch
        miliSwitch = binding.militaryswitch
       // theme = binding.themeSet
        notifSwitch.setOnClickListener {
            if (notifSwitch.isChecked) {
                Toast.makeText(this@SettingsActivity, "Notification turned ON", Toast.LENGTH_SHORT).show()

            }
            else{
                Toast.makeText(this@SettingsActivity, "Notification turned OFF", Toast.LENGTH_SHORT).show()
            }
        }

        miliSwitch.setOnClickListener{
            if (miliSwitch.isChecked) {
                Toast.makeText(this@SettingsActivity, "Done", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("gomili", "yes")
                startActivity(intent)
                finish()
            }
            else{
                Toast.makeText(this@SettingsActivity, "OFF", Toast.LENGTH_SHORT).show()
            }
        }


//        theme.setOnClickListener {
//            DialogManager.showCustomAlertDialog(this, object : ColorDialogCallback {
//                override fun onChosen(chosenColor: String) {
//                    if (chosenColor == ThemeStorage.getThemeColor(applicationContext)) {
//                        Toast.makeText(
//                            this@SettingsActivity, "Theme is already chosen",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        return
//                    }
//                    Log.d(MainActivity.TAG, chosenColor)
//                    ThemeStorage.setThemeColor(applicationContext, chosenColor)
//                    ThemeManager.setCustomizedThemes(applicationContext, chosenColor)
//                    recreate()
//                }
//            })
//        }
//    }

}}