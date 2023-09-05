package com.example.updatesoftwares

import android.app.Dialog
import android.content.Intent
import android.content.IntentSender
import android.graphics.Point
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.updatesoftwares.databinding.ActivityMainBinding
import com.example.updatesoftwares.utils.MyAppUtils
import com.example.updatesoftwares.utils.MyUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.tasks.Task


class MainActivity : AppCompatActivity() {
    private var appUpdateManager: AppUpdateManager? = null
    private lateinit var installStateUpdatedListener: InstallStateUpdatedListener
    private lateinit var manager: ReviewManager

    private val MY_REQUEST_CODE = 111

    private lateinit var binding: ActivityMainBinding
    lateinit var dialog :BottomSheetDialog
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        MyAppUtils.setStatusBarColor(this@MainActivity,R.color.bgcolor,R.color.white)
        checkForAppUpdate()
        requestReviewDialog()
        dialog = BottomSheetDialog(this)
        val display: Display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val displayViewWidth_ = size.x
        //displayViewHeight_ = size.y
        val params:ViewGroup.LayoutParams = binding.navDrawer.drawer.getLayoutParams()

        params.width = displayViewWidth_ / 2+70;
        binding.navDrawer.drawer.setLayoutParams(params);
        binding.navDrawer.quit.setOnClickListener {
           exitDiologe()
        }

        binding.navDrawer.rateUs.setOnClickListener {
            MyUtils.rateApp(this@MainActivity)
        }
        binding.navDrawer.privacy.setOnClickListener {
            MyUtils.openPrivacyPolicy(this@MainActivity)
        }
        binding.navDrawer.close.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)

        }
        binding.navDrawer.share.setOnClickListener(View.OnClickListener {
            MyUtils.shareApp(this@MainActivity)
        })
        binding.navDrawer.moreApps.setOnClickListener {
            MyUtils.moreApp(this@MainActivity)
        }
        addFragment(HomeFragment())
    }



    fun  addFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frgmentFrame, fragment)
        transaction.commit()
    }



    override fun onBackPressed() {
        exitDiologe()
    }

    private fun exitDiologe() {
        //val dialog = Dialog(this)
        dialog.setContentView(R.layout.custondialog)
        val yes: TextView? = dialog.findViewById(com.example.updatesoftwares.R.id.yes)
        val no: TextView? = dialog.findViewById(com.example.updatesoftwares.R.id.no)

        no?.setOnClickListener {
            dialog.dismiss()
        }
        yes?.setOnClickListener {
            super.onBackPressed()
        }

        dialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        dialog.dismiss()
        unregisterInstallStateUpdListener()
    }
    override fun onResume() {
        super.onResume()
        checkNewAppVersionState()
    }


// in app update methods

    private fun checkForAppUpdate() {
        // Creates instance of the manager.
        appUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        // Returns an intent object that you use to check for an update.
//        AppUpdateManager appUpdateInfoTask =  appUpdateManager.getAppUpdateInfo();

        // Create a listener to track request state updates.
        installStateUpdatedListener =
            InstallStateUpdatedListener { installState ->
                // Show module progress, log state, or install the update.
                if (installState.installStatus() == InstallStatus.DOWNLOADED)                         // After the update is downloaded, show a notification
                // and request user confirmation to restart the app.
                    popupSnackbarForCompleteUpdateAndUnregister()
            }

        // Checks that the platform will allow the specified type of update.
        appUpdateManager!!.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo?.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                // Request the update.
                if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {

                    // Before starting an update, register a listener for updates.
                    appUpdateManager!!.registerListener(installStateUpdatedListener)
                    // Start an update.
                    startAppUpdateFlexible(appUpdateInfo)
                }
            }
        }
    }

    private fun startAppUpdateFlexible(appUpdateInfo: AppUpdateInfo) {
        try {
            appUpdateManager!!.startUpdateFlowForResult(
                appUpdateInfo,
                AppUpdateType.FLEXIBLE,
                this,
                MY_REQUEST_CODE
            )
        } catch (e: IntentSender.SendIntentException) {
            e.printStackTrace()
            unregisterInstallStateUpdListener()
        }
    }

    private fun popupSnackbarForCompleteUpdateAndUnregister() {
        val parentLayout = findViewById<View>(android.R.id.content)
        val snackbar = Snackbar.make(
            parentLayout,
            getString(R.string.update_downloaded),
            Snackbar.LENGTH_INDEFINITE
        )
        snackbar.setAction(
            R.string.restart, View.OnClickListener { appUpdateManager!!.completeUpdate() }
        )
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
        snackbar.show()
        unregisterInstallStateUpdListener()
    }

    private fun checkNewAppVersionState() {
        appUpdateManager!!.appUpdateInfo.addOnSuccessListener { appUpdateInfo -> //FLEXIBLE:
            // If the update is downloaded but not installed,
            // notify the user to complete the update.
            if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackbarForCompleteUpdateAndUnregister()
            }
        }
    }

    private fun unregisterInstallStateUpdListener() {
        if (appUpdateManager != null) appUpdateManager!!.unregisterListener(
            installStateUpdatedListener
        )
    }
    // in-app review
    private fun requestReviewDialog() {
        manager = ReviewManagerFactory.create(this)
        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { task: Task<ReviewInfo?> ->
            if (task.isSuccessful) {
                // We can get the ReviewInfo object
                val reviewInfo = task.result
                launchReview(reviewInfo)
            } else {
                // There was some problem, log or handle the error code.
             //  @ReviewErrorCode int reviewErrorCode = ((TaskException) task.getException()).getErrorCode();
            }
        }
    }

    private fun launchReview(reviewInfo: ReviewInfo) {
        val flow: Task<Void> = manager.launchReviewFlow(this, reviewInfo)
        flow.addOnCompleteListener { task -> }
    }

}