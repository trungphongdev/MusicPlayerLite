package com.example.musicplayerlite.permission

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.lang.ref.WeakReference

class PermissionHelper {
    val readMediaAudioPermission =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }


    fun isPermissionGranted(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun isAllPermissionGranted(context: Context, vararg permission: String): Boolean {
        return permission.all { isPermissionGranted(context, it) }
    }

    fun permissionLauncher(
        fragment: Fragment,
        onGranted: () -> Unit,
        onDenied: () -> Unit,
    ): ActivityResultLauncher<String>? {
        return WeakReference(fragment).get()?.registerForActivityResult(
            RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                onGranted()
            } else {
                onDenied()
            }
        }
    }

    fun checkAndRequestPermission(
        context: Context,
        permission: String,
        requestPermissionLauncher: ActivityResultLauncher<String>,
        callback: PermissionCallback,
    ) {
        WeakReference(context).get()?.let {
            when {
                isPermissionGranted(it, permission) -> {
                    callback.onGranted()
                }

                ActivityCompat.shouldShowRequestPermissionRationale(it as Activity, permission) -> {
                    callback.onRationale()
                }

                else -> {
                    requestPermissionLauncher.launch(permission)
                }
            }
        }
    }

}
