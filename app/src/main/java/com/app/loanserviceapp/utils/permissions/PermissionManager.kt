package com.app.loanserviceapp.utils.permissions

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.app.loanserviceapp.utils.AlertManagerUtils
import com.diageo.edge.utils.LogUtil

class PermissionManager {

    companion object {
        enum class PERMISSION_SETS {
            IMAGE_PICK,
            WRITE_READ_STORAGE
        }
        //private val PERMISSION_SET_FOR_LOCATION = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        private val PERMISSION_SET_FOR_IMAGE_PICK = arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
        private val PERMISSION_SET_FOR_WRITE_READ_STORAGE = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)

        private val PERMISSIONS = arrayOf(*PERMISSION_SET_FOR_IMAGE_PICK, *PERMISSION_SET_FOR_WRITE_READ_STORAGE)

        private const val REQUEST_CODE_FOR_ALL = 3000
        private const val PERMISSION_TITLE = "Permission required!!"
        private const val PERMISSION_MESSAGE = "We need permission for this application please grant or deny"
        private const val PERMISSION_POSITIVE_BUTTON = "Grant"
        private const val PERMISSION_NEGATIVE_BUTTON = "Deny"
        private const val PERMISSION_NOT_GRANTED_TITLE = "Permission needed!!"
        private const val PERMISSION_NOT_GRANTED_MESSAGE = "We notice, you haven't give the permissions that " +
            "we need, so please visit setting of application and give the required permissions"
        private const val PERMISSION_NOT_GRANTED_POSITIVE_BUTTON = "OK"
        private const val PERMISSION_NOT_GRANTED_NEGATIVE_BUTTON = "CANCEL"

        /**
         * checking all permissions that are passed to the function
         * @param context Passing the context to check permission
         * @param permissions Variable argument permissions that are available in String format
         * it will check for all and then it will return a boolean value from it
         * */
        fun hasPermissions(context: Context, vararg permissions: String): Boolean = permissions.all {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

        /**
         * It will take permission for only one permission string
         * @param permission Permission string to take permission
         * */
        fun requestPermissionForOne(activity: Activity, vararg permissions: String) {
            if (permissions.isNotEmpty()) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions.firstOrNull() ?: "") ||
                    ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions.firstOrNull() ?: "")
                ) {
                    AlertManagerUtils
                        .showAlertDialog(
                            activity,
                            message = PERMISSION_MESSAGE,
                            isPosButtonEnabled = true,
                            isNegativeButtonEnabled = true,
                            posListener = {
                                ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE_FOR_ALL)
                            }
                        )
                } else {
                    ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE_FOR_ALL)
                }
            }
        }

        /**
         * request permission for all the device specific permission listed upper as *PERMISSIONS*
         * It will first check for permission is already given or not then it will request the permission sequential
         * */
        fun requestAllRequiredPermissions(activity: Activity) {
            if (!hasPermissions(activity, *PERMISSIONS))
                ActivityCompat.requestPermissions(activity, PERMISSIONS, REQUEST_CODE_FOR_ALL)
        }

        private fun showPromptIfPermissionIsNotAvailable(context: Context) {
            AlertDialog.Builder(context).apply {
                setTitle(PERMISSION_NOT_GRANTED_TITLE)
                setMessage(PERMISSION_NOT_GRANTED_MESSAGE)
                setPositiveButton(PERMISSION_NOT_GRANTED_POSITIVE_BUTTON) { dialog, _ ->
                    dialog.dismiss()
                }
                setNegativeButton(PERMISSION_NOT_GRANTED_NEGATIVE_BUTTON) { dialog, _ ->
                    dialog.dismiss()
                }
                create()
            }.show()
        }
        /**
         * It's for checking permission for predefined permissions that are available in this class
         * @param permissionSets Permission set type to check for set of permissions
         * */
        fun checkPermissionForSet(context: Context, permissionSets: PERMISSION_SETS, showPrompt: Boolean = true): Boolean {
            return when (permissionSets) {
               /* PERMISSION_SETS.LOCATION -> {
                    if (!hasPermissions(context, *PERMISSION_SET_FOR_LOCATION)) {
                        if (showPrompt)
                            showPromptIfPermissionIsNotAvailable(context)
                        LogUtil.debug(" 🥳 permission not granted!!")
                        false
                    } else {
                        true
                    }
                }*/
                PERMISSION_SETS.IMAGE_PICK -> {
                    if (!hasPermissions(context, *PERMISSION_SET_FOR_IMAGE_PICK)) {
                        if (showPrompt)
                            showPromptIfPermissionIsNotAvailable(context)
                        false
                    } else {
                        true
                    }
                }
                PERMISSION_SETS.WRITE_READ_STORAGE -> {
                    if (!hasPermissions(context, *PERMISSION_SET_FOR_WRITE_READ_STORAGE)) {
                        if (showPrompt)
                            showPromptIfPermissionIsNotAvailable(context)
                        false
                    } else {
                        true
                    }
                }
            }
        }
    }
}
