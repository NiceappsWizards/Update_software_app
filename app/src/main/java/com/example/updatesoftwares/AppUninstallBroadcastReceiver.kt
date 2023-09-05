package com.example.updatesoftwares

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_PACKAGE_FULLY_REMOVED
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.updatesoftwares.Interfaces.OnUninstall
import com.example.updatesoftwares.adapters.UnInsatallAppAdopter
import kotlin.properties.Delegates

open class AppUninstallBroadcastReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            ACTION_PACKAGE_FULLY_REMOVED -> {
                Log.d("removedPkg", "removedeeee")
                Log.d("pos",UnInsatallAppAdopter.pos.toString())
                UnInsatallAppAdopter.pos?.let { UnInsatallAppAdopter.onInsatalListner?.onUninstall(it) }

            }

        }
    }
}