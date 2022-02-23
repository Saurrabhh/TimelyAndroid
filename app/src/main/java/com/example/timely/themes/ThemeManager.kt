package com.example.timely.themes

import android.content.Context
import com.example.timely.R

class ThemeManager {

    companion object {
        fun setCustomizedThemes(context: Context, theme: String?) {
            when (theme) {
                "grey" -> context.setTheme(R.style.Theme_Timely_Grey)
                "red" -> context.setTheme(R.style.Theme_Timely_Red)
                "purple" -> context.setTheme(R.style.Theme_Timely_Purple)
                "green" -> context.setTheme(R.style.Theme_Timely_Green)
                "blue" -> context.setTheme(R.style.Theme_Timely_Blue)
                "orange" -> context.setTheme(R.style.Theme_Timely_Orange)
                "pink" -> context.setTheme(R.style.Theme_Timely_Pink)
                "yellow" -> context.setTheme(R.style.Theme_Timely_Yellow)
            }
        }
    }
}