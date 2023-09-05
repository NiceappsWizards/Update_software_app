package com.example.updatesoftwares

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.inflate
import com.example.updatesoftwares.databinding.ActivityDiviceInfoBinding
import com.example.updatesoftwares.databinding.ActivityDiviceInfoBinding.inflate
import com.example.updatesoftwares.databinding.ActivityMainBinding
import com.example.updatesoftwares.utils.MyAppUtils

class DiviceInfo : AppCompatActivity() {
    private lateinit var binding: ActivityDiviceInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiviceInfoBinding.inflate(layoutInflater)
        val view = binding.root
        MyAppUtils.setStatusBarColor(this@DiviceInfo,R.color.bgcolor,R.color.white)
        setContentView(view)
        binding.model.text =Build.MODEL
        binding.version.text = Build.VERSION.RELEASE
        binding.Ide.text =Build.ID
        binding.brand.text = Build.BRAND
        binding.broad.text = Build.BOARD
        binding.device.text = Build.DEVICE
        binding.Hardware.text = Build.HARDWARE
        binding.manufac.text = Build.MANUFACTURER
        binding.arrowback.setOnClickListener(View.OnClickListener {
            finish()
        })

    }
}