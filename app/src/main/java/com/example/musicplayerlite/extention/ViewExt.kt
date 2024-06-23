package com.example.musicplayerlite.extention

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Shader
import android.os.Build
import android.widget.TextView

fun TextView.showStrikeThrough(show: Boolean) {
    paintFlags = if (show)
        paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    else
        paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()

}

fun Context.copyTextToClipBoard(label: String = "prompt", text: String) {
    val clipboard: ClipboardManager =
        this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(/* label = */ label, /* text = */ text)
    clipboard.setPrimaryClip(clip)
}

fun TextView.setGradientText(startColor: Int, endColor: Int) {
    val colors = intArrayOf(startColor, endColor)
    val positions = floatArrayOf(0f, 1f)

    val bound = Rect()
    val textWidth = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        paint.getTextBounds(this.text, 0, this.text.length, bound)
        bound.width().toFloat()
    } else {
        (this.textSize / this.length().toFloat()) * 10f
    }

    val textShader: Shader = LinearGradient(
        0f,
        0f,
        textWidth,
        0f,
        colors,
        positions,
        Shader.TileMode.CLAMP
    )
    paint.setShader(textShader)
}