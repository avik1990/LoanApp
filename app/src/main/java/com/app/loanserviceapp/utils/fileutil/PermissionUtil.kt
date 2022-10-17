package com.app.loanserviceapp.utils.fileutil

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

class PermissionUtil constructor() {
    fun verifyPermissions(activity: Activity): Boolean? {

        // This will return the current Status
        val permissionExternalMemory: Int = ActivityCompat.checkSelfPermission(activity, WRITE_EXTERNAL_STORAGE)
        if (permissionExternalMemory != PackageManager.PERMISSION_GRANTED) {
            val STORAGE_PERMISSIONS = arrayOf<String>(WRITE_EXTERNAL_STORAGE)
            // If permission not granted then ask for permission real time.
            ActivityCompat.requestPermissions(activity, STORAGE_PERMISSIONS, 1)
            return false
        }
        return true
    }
}
