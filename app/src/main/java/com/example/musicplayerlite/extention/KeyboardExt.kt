package com.example.musicplayerlite.extention

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun showKeyBoard(view: View) {
    val manager: InputMethodManager =
        view.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    manager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}

fun hideKeyBoard(view: View) {
    val manager: InputMethodManager =
        view.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    manager.hideSoftInputFromWindow(view.applicationWindowToken, 0)
}