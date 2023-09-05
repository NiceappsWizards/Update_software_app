package com.example.updatesoftwares.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.updatesoftwares.AppUninstallBroadcastReceiver
import com.example.updatesoftwares.Interfaces.OnUninstall
import com.example.updatesoftwares.Model
import com.example.updatesoftwares.R

class UnInsatallAppAdopter(private val InfoList: ArrayList<Model>,
                           val contex: Context,
                           val onUnInstallItemListner:OnUninstall) :
    RecyclerView.Adapter<UnInsatallAppAdopter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View
             =  LayoutInflater.from(parent.context).inflate(R.layout.uninstallview,parent,false)

        return  ViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = InfoList[position]
        holder.imagv.setImageDrawable(currentItem.icon)
        holder.apname.text = currentItem.appName
        holder.appsize.text = currentItem.appSize
        holder.version.text = currentItem.versionName

            holder.btn1.setOnClickListener(View.OnClickListener {
                val intent = Intent(Intent.ACTION_DELETE)
                intent.data = Uri.parse("package:"+currentItem.pakageName)
                startActivity(contex,intent,null)
              //AppUninstallBroadcastReceiver(position,onUnInstallItemListner)
                pos = position
                onInsatalListner = onUnInstallItemListner
            //onUnInstallItemListner.onUninstall(position)
                //notifyDataSetChanged()

            })

    }

    override fun getItemCount(): Int {
        // Log.e("siz",InfoList.size.toString())
        return  InfoList.size
    }
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val imagv = itemView.findViewById<ImageView>(R.id.imageView)
        val apname = itemView.findViewById<TextView>(R.id.appname)
        val appsize = itemView.findViewById<TextView>(R.id.sappSize)
        val version = itemView.findViewById<TextView>(R.id.version)
        var btn1 = itemView.findViewById<Button>(R.id.uninstallapp)
    }
   companion object {
       var pos:Int? = null
       var onInsatalListner: OnUninstall? = null
   }
}

