package com.example.updatesoftwares

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.updatesoftwares.databinding.ActivityScanAppsBinding
import com.example.updatesoftwares.utils.MyAppUtils
import com.google.android.material.snackbar.Snackbar
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.EOFException
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


class ScanApps : AppCompatActivity() {
    lateinit var binding: ActivityScanAppsBinding
    private lateinit var AllAppList: ArrayList<ApplicationInfo>
    lateinit var newupdatedDate: Date
    lateinit var lastModifiedDate: Date
    private var appDataList = ArrayList<Model>()
    lateinit var viewModel: ViewModelclass
    lateinit var progressDialog:Dialog
//    lateinit var  progressDialog: ProgressDialog
    val launchableInstalledApps: MutableList<ApplicationInfo> = ArrayList()
    var exit = false
    var wait = false

    var i = 0
    var z =1

    ///val updateAvailableList1: MutableList<ApplicationInfo> = ArrayList()

    companion object {
        val updateAvailableList: ArrayList<Model> = ArrayList()
    }
    //val UAlist = MutableLiveData<List<Model>>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanAppsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        MyAppUtils.setStatusBarColor(this@ScanApps,R.color.bgcolor,R.color.white)
        binding.scaning.visibility = View.GONE
        updateAvailableList.clear()
       /* val intent = intent
        val bundle: Bundle? = intent.extras*/
        viewModel = ViewModelProvider(this)[ViewModelclass::class.java]
       // systemAndDownloadedAppsSeparator()

        progressDialog = Dialog(this)
        progressDialog.setContentView(R.layout.loading_dialog)
        progressDialog.show()
        val thread: Thread = object : Thread() {
            override fun run() {
                try {
                   systemAndDownloadedAppsSeparator()
                } catch (e: PackageManager.NameNotFoundException) {
                    e.printStackTrace()
                }
                runOnUiThread {
                    if (checkInternetAvailability()){
                        binding.scaning.visibility = View.VISIBLE
                        appDataList=   viewModel.getInto(this@ScanApps,AllAppList)
                        PrepareAppsData().execute()
                    }
                    else{
                        Toast.makeText(this@ScanApps,"You are not connected to Internet",Toast.LENGTH_LONG).show()
                        finish()
                    }
                    progressDialog.dismiss()
                }
            }
        }
        thread.start()

        binding.arrowback.setOnClickListener(View.OnClickListener {
         backPress()
        })



    }
    private fun systemAndDownloadedAppsSeparator() {

        AllAppList= ArrayList()
        val flag = PackageManager.GET_META_DATA or
                PackageManager.GET_SHARED_LIBRARY_FILES or
                PackageManager.GET_UNINSTALLED_PACKAGES
        val listOfAllapps =
            packageManager
                .getInstalledApplications(flag)
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
                AllAppList.add(apInfo)

            }


        }
         /*runOnUiThread {
                    progressDialog.dismiss()
                }*/

    }
    private fun checkInternetAvailability() : Boolean{
         try {
             val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
             val activeNetwork = cm.activeNetworkInfo
             if (activeNetwork != null) {
                 if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) {
                     if (activeNetwork.state == NetworkInfo.State.CONNECTED) {
                         return  true

                     }
                 } else if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) {
                     if (activeNetwork.state == NetworkInfo.State.CONNECTED) {
                         return  true

                     }
                 }
             }
         }
         catch (e:Exception){
             Snackbar.make(findViewById(android.R.id.content),e.toString(), Snackbar.LENGTH_LONG).setActionTextColor(
             Color.RED).show();
         }


        return false
    }

    private fun backPress(){
    if (checkInternetAvailability()){
        pause()
        AlertDialog.Builder(this).apply {
            setTitle("Please confirm.")
            setMessage("Are you want to stop the process?")

            setPositiveButton("Stop") { _, _ ->
                // if user press yes, then finish the current activity
                stop()
            }

            setNegativeButton("No"){_, _ ->
                // if user press no, then return the activity
                resume()
            }

            setCancelable(true)
        }.create().show()
    }
    else{
        super.onBackPressed()
    }

}
    private fun convertLongToTime(time: Long): String {

        val format = SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time
        return format.format(calendar.time)
    }

    fun  stop(){

        exit = true
    }
    fun pause(){
        wait = true
    }
    fun resume(){
        wait = false
    }

    override fun onBackPressed() {
 backPress()    }

    private fun checkUpdate() {

       val run = Runnable {

               while(AllAppList.isNotEmpty() && (i <= (AllAppList.size - 1)) && !exit)
               {
                   try {
                       println("hello for while")
                       if(!wait){
                           println("hello not wait")
                           if (i!= z){
                               z = i

                               val time = packageManager.getPackageInfo(appDataList[i].pakageName, 0).lastUpdateTime
                               val lastModifiedTime = convertLongToTime(time)

                               lastModifiedDate   = SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH).parse(lastModifiedTime)



                               Log.e("lastModifiedDate", lastModifiedDate.toString())
                               Log.e("packageNAME",appDataList[i].pakageName)
                               runOnUiThread {  binding.tolatupdatefound.text =
                                   "Total Update Found:" +"      "+updateAvailableList.size
                                   binding.checkingUpdates.text =
                                       "Checking Updates:" +"      "+i + "/" + (AllAppList.size-1)
                                   binding.appicon.setImageDrawable(appDataList[i].icon)
                                   binding.appName1.text = appDataList[i].appName  }

                               val statuscode = CheckAppLiveOnPlayStore(appDataList[i]).execute().get()
                               Log.e("status code", statuscode)
                               if (statuscode.toInt() == 200) {

                                   VersionCracker(appDataList[i]).execute()

                               }
                               else{
                                   i++
                               }
                           }
                       }

                   }catch (e:Exception){
                   e.printStackTrace()
               }

               }

           if(exit){
               finish()
           }
           else{
               runOnUiThread { val intent = Intent(this, UpdateAvalableList::class.java)
                   startActivity(intent)
                   finish()
           }

            }

        }

       val  objBgThread = Thread(run)
        objBgThread.start()
        //UAlist.value = updateAvailableList

    }
   inner class PrepareAppsData() : AsyncTask<Void, Void, String>() {
       val progressDialog = ProgressDialog(this@ScanApps)
        override fun doInBackground(vararg params: Void?): String? {


            return null
        }

        override fun onPreExecute() {
            super.onPreExecute()
            progressDialog.setTitle("wait ")
            progressDialog.setMessage("Preparing apps data, please wait")
            progressDialog.show()

        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            progressDialog.dismiss()
            checkUpdate()
        }
    }
    inner class CheckAppLiveOnPlayStore(val app: Model) :
        AsyncTask<String, String, String>() {


        override fun doInBackground(vararg p0: String?): String? {
            var responseStatus: Int? = null
               try {
                   val conn = URL("https://play.google.com/store/apps/details?id=${app.pakageName}")
                       .openConnection() as HttpURLConnection
                   // conn.useCaches = false
                   conn.connect()
             responseStatus = conn.responseCode
                   conn.disconnect()
               }catch (e:EOFException){
                   e.printStackTrace()
               }


            // Log.e("status",status.toString())

            return responseStatus.toString()
        }
    }

    inner class VersionCracker(val app: Model) : AsyncTask<String, String, String>() {



        override fun doInBackground(vararg p0: String?): String? {

            try {
                val document: Document? =
                    Jsoup.connect("https://play.google.com/store/apps/details?id=${app.pakageName}")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
               // Log.e("helloo", "helloo")
                if (document != null) {

                    val element: Elements = document.getElementsByClass("lXlx5")
                  //  Log.e("element", element.toString())
                    for (ele in element) {
                        if (ele.siblingElements() != null) {
                            val sibElemets: Elements = ele.siblingElements()
                            for (sibElemet in sibElemets) {
                                val newUpdatedDate = sibElemet.text()
                                //Log.e("newupdatedDate1222", newUpdatedDate)
                               // val formatter =
                                   // DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH)
                                newupdatedDate = SimpleDateFormat("MMM d, yyyy",Locale.ENGLISH).parse(newUpdatedDate)

                               // Log.e("newupdatedDate", newupdatedDate.toString())


                                if (lastModifiedDate < newupdatedDate)
                                {
                                    updateAvailableList.add(app)
                                    i++

                                }
                               else{
                                   i++
                               }

                            }
                        }
                    }

                }
            } catch (e: EOFException) {
                e.printStackTrace()
            }

            return null
        }


    }
}







