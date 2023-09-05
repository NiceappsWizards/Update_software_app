package com.example.updatesoftwares.utils

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.provider.Settings
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity


object MyAppUtils {

    var IS_FIRST = true
    fun setStatusBarColor(activity: AppCompatActivity, colorStatusBar: Int, colorNavBar: Int) {
        val window = activity.window
        window.statusBarColor = ContextCompat.getColor(activity, colorStatusBar)
        window.navigationBarColor = ContextCompat.getColor(activity, colorNavBar)
    }

    fun setFragStatusBarColor(activity: FragmentActivity, colorStatusBar: Int, colorNavBar: Int) {
        val window = activity.window
        window.statusBarColor = ContextCompat.getColor(activity, colorStatusBar)
        window.navigationBarColor = ContextCompat.getColor(activity, colorNavBar)
    }

}