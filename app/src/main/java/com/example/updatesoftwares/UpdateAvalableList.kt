package com.example.updatesoftwares

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.updatesoftwares.adapters.ExampleAdopter
import com.example.updatesoftwares.databinding.ActivityUpdateAvalableListBinding
import com.example.updatesoftwares.utils.MyAppUtils

class UpdateAvalableList : AppCompatActivity() {
    lateinit var binding: ActivityUpdateAvalableListBinding
    lateinit var recyclerView: RecyclerView
    lateinit var viewModel: ViewModelclass
    lateinit var  progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateAvalableListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        MyAppUtils.setStatusBarColor(this@UpdateAvalableList,R.color.bgcolor,R.color.white)
        progressBar = binding.progressBaar
        progressBar.visibility = View.VISIBLE
        thread()

        recyclerView = binding.recycler
        recyclerView.layoutManager = LinearLayoutManager(this)
        binding.heading.text = " Available Updates"
        binding.arrowback.setOnClickListener(View.OnClickListener {
            finish()
        })
        /*val intent = intent
        var bundle :Bundle ?=intent.extras
        if (bundle == null){
            Toast.makeText(this,"Sorry no bundle Found", Toast.LENGTH_SHORT).show()
        }
        else {

            appList = bundle.getParcelable<Parcelable>("AppInfoList") as ArrayList<Model>
            Log.e("Applist",appList.size.toString())
            Log.e("code",appList.toString())
        }*/
        viewModel = ViewModelProvider(this).get(ViewModelclass::class.java)
        viewModel.infoList1.value = ScanApps.updateAvailableList
        observer()
        //viewModel.getInto(this,appList)

    }
    fun  thread(){
        val run  = Runnable {
            Thread.sleep(1500)
            runOnUiThread{
                progressBar.visibility = View.GONE

            }

        }
        val thrd = Thread(run)
        thrd.start()

    }
    private fun observer(){

        viewModel.infoList1.observe(this, Observer {
            it
            recyclerView.adapter = ExampleAdopter(it, 3, this)
            //txv.text = "Number of "+ str + " apps ${it.size}"
        })
    }
}
