package com.example.updatesoftwares

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.updatesoftwares.Inter_Splash.Inter_Splash.*
import com.example.updatesoftwares.databinding.ActivitySplashBinding
import com.example.updatesoftwares.utils.MyAppUtils
import com.example.updatesoftwares.utils.MyUtils



class Splash : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding
    var adDecided = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        load_interstitial_splash()
         binding.progress.visibility = View.VISIBLE
        binding.getStart.visibility= View.INVISIBLE

        Thread()
        MyAppUtils.setStatusBarColor(this@Splash, R.color.bgcolor, R.color.white)
        val getsatrd = binding.getStart
        getsatrd.visibility = View.GONE

        binding.privacyPolicy.setOnClickListener {
            MyUtils.openPrivacyPolicy(this@Splash)
        }
        getsatrd.setOnClickListener {
            if (InterstitialMasterSplash.isAlreadyLoaded) {
                InterstitialMasterSplash.show_insterstitial(this@Splash)
                InterstitialClosedListenerImplementerMasterSplash.setOnInterstitialClosedMaster(
                    object :
                        InterstitialClosedListenerMasterSplash {
                        override fun onInterstitialClosed() {

                            startbutton()
                        }

                        override fun onInterstitialFailedToShow() {
                            startbutton()
                        }
                    })
            } else {
                startbutton()
            }
        }

    }

    private fun load_interstitial_splash() {
        InterstitialMasterSplash.load_interstitial(this, getString(R.string.interstitialSplash))
        InterstitialLoadedListenerImplementerMasterSplash.setOnInterstitialLoadedMaster(object :
            InterstitialLoadedListenerMasterSplash {
            override fun onInterstitialLoaded() {
                adDecided = true
               //   showStartButton()
            }

            override fun onInterstitialFailed() {
                adDecided = true
                //  showStartButton()
            }
        })
    }

    private fun startbutton() {
        startActivity(
            Intent(
                this@Splash,
                MainActivity::class.java
            )
        )
        overridePendingTransition(0, 0)
        finish()
    }

    fun Thread() {
        val run = Runnable {
            Thread.sleep(3000)
            runOnUiThread {
                binding.progress.visibility = View.INVISIBLE
                binding.getStart.visibility = View.VISIBLE
            }

        }
        val thrd = Thread(run)
        thrd.start()

    }
}