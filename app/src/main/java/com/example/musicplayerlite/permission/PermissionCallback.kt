package com.example.musicplayerlite.permission

interface PermissionCallback {
    fun onGranted()
    fun onDenied()

    fun onRationale()
}
