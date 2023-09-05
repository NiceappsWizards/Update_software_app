package com.example.updatesoftwares

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable


data class Model(var appName:String, val appSize:String, val versionName:String, val icon: Drawable, var pakageName: String)