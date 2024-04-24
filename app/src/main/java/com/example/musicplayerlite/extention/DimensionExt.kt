package com.example.musicplayerlite.extention

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics

fun screenWidth() = Resources.getSystem().displayMetrics.widthPixels
fun screenHeight() = Resources.getSystem().displayMetrics.heightPixels

fun Context.toPx(dp: Float): Int {
    return (dp * (resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
}

fun Context.toDp(px: Int): Float {
    return px / (resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}