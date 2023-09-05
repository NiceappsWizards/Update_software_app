package com.example.updatesoftwares.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast

import com.example.updatesoftwares.R


object MyUtils {
    private const val TAG: String = "Debug"

    fun loadVideo(context: Context) {
        try {
            val mintent = Intent(Intent.ACTION_VIEW)
            mintent.data =
                Uri.parse("")
            context.startActivity(mintent)
        } catch (e1: java.lang.Exception) {
            try {
                val uriUrl = Uri
                    .parse("")
                val launchBrowser = Intent(
                    Intent.ACTION_VIEW,
                    uriUrl
                )
                context.startActivity(launchBrowser)
            } catch (e2: java.lang.Exception) {
                Toast.makeText(
                    context,
                    "No Application Found to open link",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun moreApp(context: Context) {
        try {
            val mintent = Intent(Intent.ACTION_VIEW)
            mintent.data =
                Uri.parse("")
            context.startActivity(mintent)
        } catch (e1: java.lang.Exception) {
            try {
                val uriUrl = Uri
                    .parse("")
                val launchBrowser = Intent(
                    Intent.ACTION_VIEW,
                    uriUrl
                )
                context.startActivity(launchBrowser)
            } catch (e2: java.lang.Exception) {
                Toast.makeText(
                    context,
                    "No Application Found to open link",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun rateApp(context: Context) {
        try {
            val mintent = Intent(Intent.ACTION_VIEW)
            mintent.data = Uri.parse(
                "market://details?id="
                        + context.packageName
            )
            context.startActivity(mintent)
        } catch (e1: Exception) {
            Toast.makeText(
                context,
                "No Application Found to open link",
                Toast.LENGTH_SHORT
            ).show()

        }
    }

    fun shareApp(context: Context) {
        val shareit = Intent(Intent.ACTION_SEND)
        shareit.type = "text/plain"
        shareit.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
        shareit.putExtra(
            Intent.EXTRA_TEXT,
            (context.resources.getString(R.string.app_name) + ":\nhttps://play.google.com/store/apps/details?id="
                    + context.packageName)
        )
        context.startActivity(shareit)
    }

    fun openPrivacyPolicy(context: Context) {
        val uri =
            Uri.parse("https://sites.google.com/view/appsiumsoftwareupdateapp/home")
        val myAppLinkToMarket = Intent(Intent.ACTION_VIEW, uri)
        try {
            context.startActivity(myAppLinkToMarket)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                context,
                "Impossible to find an application for the market",
                Toast.LENGTH_LONG
            ).show()
        }

    }

    fun updateApp(context: Context) {
        try {
            val mintent = Intent(Intent.ACTION_VIEW)
            mintent.data = Uri.parse(
                "market://details?id="
                        + context.packageName
            )
            context.startActivity(mintent)
        } catch (e1: Exception) {
            Toast.makeText(
                context,
                "No Application Found to open link",
                Toast.LENGTH_SHORT
            ).show()

        }
    }


}