package com.example.updatesoftwares.adapters


import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.updatesoftwares.Model
import com.example.updatesoftwares.R



class ExampleAdopter(
    private val InfoList: List<Model>,
    val code: Int,
    val contex: Context,
):
    RecyclerView.Adapter<ExampleAdopter.ExampleViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExampleViewHolder {
        val itemView: View
        when (code){
            3 ->  itemView = LayoutInflater.from(parent.context).inflate(R.layout.updateview,parent,false)
            else -> itemView =  LayoutInflater.from(parent.context).inflate(R.layout.singleitemview,parent,false)
        }


        return  ExampleViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: ExampleViewHolder, position: Int) {
       val currentItem = InfoList[position]
        holder.imagv.setImageDrawable(currentItem.icon)
        holder.apname.text = currentItem.appName
        holder.appsize.text = currentItem.appSize
        holder.version.text = currentItem.versionName

        Log.e("code",code.toString())



           // Log.e("code",code.toString())
            holder.btn.setOnClickListener {
                try {
                    val  intent = Intent()
                    intent.action = Intent.ACTION_VIEW
                    intent.setData(Uri.parse("market://details?id="+currentItem.pakageName ))
                    contex.startActivity(intent)
                    Log.e("dataset", "try")
                }
                catch (e:ActivityNotFoundException){
                    Log.e("dataset", "catch")
                    e.printStackTrace()
                    /*val  intent = Intent()
                    intent.action = Intent.ACTION_VIEW
                   val dataset = Uri.parse("https://play.google.com/store/apps/details?id="+currentItem.pakageName)
                   intent.setData(dataset)
                    Log.e("dataset", dataset.toString())
                    contex.startActivity(intent)*/
                }

            }



    }

    override fun getItemCount(): Int {
       // Log.e("siz",InfoList.size.toString())
    return  InfoList.size
    }

    class ExampleViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val imagv = itemView.findViewById<ImageView>(R.id.imageView)
        val apname = itemView.findViewById<TextView>(R.id.appname)
        val appsize = itemView.findViewById<TextView>(R.id.sappSize)
        val version = itemView.findViewById<TextView>(R.id.version)
        val  btn = itemView.findViewById<Button>(R.id.updateCkeck)


    }

}

