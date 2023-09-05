package com.example.updatesoftwares

import android.app.Dialog
import android.app.Notification
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.updatesoftwares.Interfaces.OnUninstall
import com.example.updatesoftwares.adapters.ExampleAdopter
import com.example.updatesoftwares.adapters.UnInsatallAppAdopter
import com.example.updatesoftwares.databinding.ActivityDetailsBinding
import com.example.updatesoftwares.utils.MyAppUtils
import java.io.File


class DetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailsBinding
    lateinit var recyclerView: RecyclerView
    lateinit var progressDialog: Dialog

    var code = ""
    var appList: ArrayList<ApplicationInfo> = ArrayList()
    private lateinit var listOfAllapps: ArrayList<ApplicationInfo>
    val launchableInstalledApps: ArrayList<ApplicationInfo> = ArrayList()
    val installedAppInfo: ArrayList<ApplicationInfo> = ArrayList()
    val systemAppInfo: ArrayList<ApplicationInfo> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val onUnInstallItemListner = object : OnUninstall {
            override fun onUninstall(pos: Int) {
                Log.e("uinstallled", pos.toString())
                if (pos < appList.size) {
                    Log.e("uinstallled", "uinstallledwww")
                    appList.removeAt(pos)

                }
            }
        }
        MyAppUtils.setStatusBarColor(this@DetailsActivity, R.color.bgcolor, R.color.white)
        val brc = AppUninstallBroadcastReceiver()
        val filter = IntentFilter()

        filter.addAction(Intent.ACTION_PACKAGE_FULLY_REMOVED)
        registerReceiver(brc, filter)
        binding.arrowback.setOnClickListener {
            finish()
        }
        progressDialog = Dialog(this)
        progressDialog.setContentView(R.layout.loading_dialog)
        progressDialog.show()
        val intent = intent
        val bundle: Bundle? = intent.extras
        if (bundle == null) {
            Toast.makeText(this, "Sorry no bundle Found", Toast.LENGTH_SHORT).show()
        } else {
            code = bundle.getString("code")!!
            // appList = bundle.getParcelableArrayList("AppInfoList")!!
            Log.e("Applist", appList.size.toString())
            binding.heading.text = bundle.getString("heading")
            Log.e("code", appList.toString())
        }

        recyclerView = binding.recycler
        recyclerView.layoutManager = LinearLayoutManager(this)

        val thread: Thread = object : Thread() {
            override fun run() {
                try {
                    systemAndDownloadedAppsSeparator()
                } catch (e: PackageManager.NameNotFoundException) {
                    e.printStackTrace()
                }

                runOnUiThread {
                    val runable = Runnable {
                        val newList = getModel()
                        runOnUiThread {
                            if (code == "2") {
                                appList = systemAppInfo
                            } else {
                                appList = installedAppInfo
                            }
                            if (code == "1") {
                                recyclerView.adapter = UnInsatallAppAdopter(
                                    newList,
                                    this@DetailsActivity,
                                    onUnInstallItemListner
                                )
                            } else {
                                recyclerView.adapter =
                                    ExampleAdopter(newList, code.toInt(), this@DetailsActivity)
                            }
                            //txv.text = "Number of "+ str + " apps ${it.size}"
                            progressDialog.dismiss()
                        }
                    }
                    val objBgThread = Thread(runable)
                    objBgThread.start()
                }
            }
        }
        thread.start()
    }

    private fun systemAndDownloadedAppsSeparator() {


        val flag = PackageManager.GET_META_DATA or
                PackageManager.GET_SHARED_LIBRARY_FILES or
                PackageManager.GET_UNINSTALLED_PACKAGES
        listOfAllapps =
            this.packageManager
                .getInstalledApplications(flag) as ArrayList<ApplicationInfo>

        for (list in listOfAllapps) {
            if (this.packageManager
                    .getLaunchIntentForPackage(list.packageName) != null
            ) {
                //If you're here, then this is a launch-able app//
                launchableInstalledApps.add(list);
            }

        }

        for (apInfo in launchableInstalledApps) {
            if (apInfo.flags and ApplicationInfo.FLAG_SYSTEM > 0) {
                // IS A SYSTEM APP
                systemAppInfo.add(apInfo)

                // Log.e("systemAppInfo",systemAppInfo.toString())
            }

            /* if (name.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP !== 0)*/
            else {
                // APP WAS INSTALL AS AN UPDATE TO A BUILD-IN SYSTEM APP
                installedAppInfo.add(apInfo)
            }


        }

    }

    private fun getModel(): ArrayList<Model> {
        val list: ArrayList<Model> = ArrayList()
        var model: Model
        if (appList.isNotEmpty()) {
            for (info in appList) {
                try {
                    val packageName = info.packageName
                    val appName = info.loadLabel(packageManager).toString();
                    val file = File(info.sourceDir)
                    val sizeInByte = file.length() // size in Byte
                    val size = (sizeInByte / 1024) / 1024
                    val versionName = packageManager.getPackageInfo(packageName, 0).versionName
                    val drawableIcon: Drawable = info.loadIcon(packageManager)
                    model = Model(
                        icon = drawableIcon,
                        appName = appName,
                        appSize = "size: $size MB",
                        versionName = "version: $versionName ",
                        pakageName = packageName
                    )

                    list.add(model)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }

        return list
    }

}