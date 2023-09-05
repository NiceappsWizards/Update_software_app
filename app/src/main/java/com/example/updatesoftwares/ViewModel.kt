package com.example.updatesoftwares

import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import java.io.File

class ViewModelclass (application: Application) : AndroidViewModel(application) {
    val infoList1 = MutableLiveData<List<Model>>()
    val contex: Context

    init {
        application.contentResolver
        contex = application
    }

    fun getInto(context: AppCompatActivity, infoList:  MutableList<ApplicationInfo>) :ArrayList<Model>{
        val List: ArrayList<Model> = ArrayList()
        val runable = Runnable {
            lateinit var model: Model

            for (info in infoList) {
                val packageName = info.packageName
                val appName = info.loadLabel(contex.packageManager).toString();
                val file = File(info.sourceDir)
                val sizeInByte = file.length() // size in Byte
               // Log.e("sizeInByte",sizeInByte.toString())
                val  size = (sizeInByte/1024)/1024
               // Log.e("size",size.toString())
                val versionName = contex.packageManager.getPackageInfo(packageName, 0).versionName
              //  Log.d("TAG", versionName)

                val drawableIcon: Drawable = info.loadIcon(contex.packageManager)
                model = Model(
                    icon = drawableIcon,
                    appName = appName,
                    appSize = "size: $size MB",
                    versionName = "version: $versionName ",
                    pakageName = packageName
                )

                List.add(model)

            }
            context.runOnUiThread {
                // Utils.hideProgressDialog(this@ImageActivity)
                // Log.d(TAG, "run: " + imageList.size())
                infoList1.value = List
             // sizee = List.size.toString()

            }
        }
        val objBgThread = Thread(runable)
        objBgThread.start()
        return List
    }
}




