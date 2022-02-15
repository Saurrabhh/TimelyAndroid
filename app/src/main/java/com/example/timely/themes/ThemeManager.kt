package com.example.timely.themes

import android.content.Context
import com.example.timely.R

class ThemeManager {

    companion object {
        fun setCustomizedThemes(context: Context, theme: String?) {
            when (theme) {
                "grey" -> context.setTheme(R.style.Theme_Timely6)
                "black" -> context.setTheme(R.style.Theme_Timely8)
                "red" -> context.setTheme(R.style.Theme_Timely2)
                "purple" -> context.setTheme(R.style.Theme_Timely4)
                "green" -> context.setTheme(R.style.Theme_Timely3)
                "blue" -> context.setTheme(R.style.Theme_Timely)
                "orange" -> context.setTheme(R.style.Theme_Timely7)
                "pink" -> context.setTheme(R.style.Theme_Timely5)
            }
        }
    }
}
