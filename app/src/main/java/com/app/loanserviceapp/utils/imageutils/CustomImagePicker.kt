package com.app.loanserviceapp.utils.imageutils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.app.loanserviceapp.R
import com.app.loanserviceapp.main.MainActivity
import com.app.loanserviceapp.utils.AlertManagerUtils
import com.app.loanserviceapp.utils.fileutil.FileUtil
import com.app.loanserviceapp.utils.fileutil.writeBitmap
import com.app.loanserviceapp.utils.permissions.PermissionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import androidx.core.app.ActivityCompat.startActivityForResult

import android.content.DialogInterface
import android.util.Log
import androidx.core.app.ActivityCompat


@RequiresApi(Build.VERSION_CODES.M)
class CustomImagePicker {
    private var activity: Activity
    private var fragment: Fragment? = null
    private var dialogFragment: DialogFragment? = null
    private var whichContext: WHICH_CONTEXT

    private var folderName: String = ""

    private var fileName: String = ""

    private var isCompressed: Boolean = false

    private var height: Int = 0

    private var width: Int = 0

    lateinit var imageCompressor: ImageCompressor

    lateinit var fileUtil: FileUtil

    var listener: ((bitmap: Bitmap, fileUri: String) -> Unit)? = null

    companion object {
        const val CAMERA_REQUEST_CODE = 101
        const val GALLERY_REQUEST_CODE = 102
        const val TEMPORARY_FOLDER_NAME = "temporary"
        const val TEMPORARY_FILE_NAME = "temporary.jpeg"
    }

    enum class WHICH_CONTEXT {
        ACTIVITY,
        FRAGMENT,
        DIALOG_FRAGMENT
    }
    /**
     * Three constructor for three types of support for custom image picker
     * 1.First-> Activity
     * 2.Second->Fragment
     * 3.Third->Dialog Fragment
     * */
    constructor(activity: Activity) {
        this.activity = activity
        whichContext = WHICH_CONTEXT.ACTIVITY
    }

    constructor(fragment: Fragment) {
        this.activity = fragment.requireActivity()
        this.fragment = fragment
        whichContext = WHICH_CONTEXT.FRAGMENT
    }
    constructor(dialogFragment: DialogFragment) {
        this.activity = dialogFragment.requireActivity()
        this.dialogFragment = dialogFragment
        whichContext = WHICH_CONTEXT.DIALOG_FRAGMENT
    }

    /**
     * Registering the listener before calling pickImage function
     * All the parameters are for special purpose
     * @param folder to save
     * @param fileName to save
     * @param isCompressed compression required or not
     * @param height if compression required then give height as required
     * @param width if compression required then give width as required
     * */
    fun registerListener(
        folder: String = "",
        fileName: String = "",
        isCompressed: Boolean = false,
        height: Int = 0,
        width: Int = 0,
        listener: (bitmap:Bitmap,fileUri: String)->Unit

    ) {
        this.folderName = folder
        this.fileName = fileName
        this.isCompressed = isCompressed
        this.height = height
        this.width = width
        this.listener = listener
    }

    fun setFileName(fileName: String) {
        this.fileName = fileName
    }

    private fun saveBitmapToDisk(bitmap: Bitmap) {
        CoroutineScope(Dispatchers.IO).launch {
            if (isCompressed && height != 0 && width != 0) {
                val fileUtil = FileUtil(getContext())
                val path = fileUtil.getFileUri(folderName, fileName)
                val file = File(path)
                file.writeBitmap(bitmap, Bitmap.CompressFormat.JPEG)
                val isCompressed = imageCompressor.compressFile(path, height = height, width = width)
                withContext(Dispatchers.Main) {
                    if (isCompressed) {
                        listener?.invoke(bitmap, path)
                    }
                }
            } else {
                val fileUtil = FileUtil(getContext())
                val path = fileUtil.getFileUri(TEMPORARY_FOLDER_NAME, TEMPORARY_FILE_NAME)
                Log.e("Path",TEMPORARY_FILE_NAME)
                val file = File(path)
                val written = file.writeBitmap(bitmap, Bitmap.CompressFormat.JPEG)
                withContext(Dispatchers.Main) {
                    if (written) {
                        listener?.invoke(bitmap, path)
                    }
                }
            }
        }
    }

    /**
     * Utility function for getting the context for three different context
     * */
    private fun getContext(): Context {
        try {
            return when (whichContext) {
                WHICH_CONTEXT.ACTIVITY -> {
                    activity
                }
                WHICH_CONTEXT.FRAGMENT -> {
                    fragment?.requireContext()!!
                }
                WHICH_CONTEXT.DIALOG_FRAGMENT -> {
                    dialogFragment?.requireContext()!!
                }
            }
        } catch (e: Exception) {
            return activity
        }
    }

    /**
     * Utility function for starting the activity for result for different contexts
     * */
    private fun startActivityResult(intent: Intent, requestCode: Int) {
        when (whichContext) {
            WHICH_CONTEXT.ACTIVITY -> {
                activity.startActivityForResult(intent, requestCode)
            }
            WHICH_CONTEXT.FRAGMENT -> {
                fragment?.startActivityForResult(intent, requestCode)
            }
            WHICH_CONTEXT.DIALOG_FRAGMENT -> {
                dialogFragment?.startActivityForResult(intent, requestCode)
            }
        }
    }

    /**
     * It's the dialog for showing the options of
     * 1.Take photo using camera ---> One can take image by camera
     * 2.Take photo using Gallery ----> One can choose from gallery also
     * */
    private fun showImageDialog() {
        val optionItems = arrayOf(getContext().resources.getString(R.string.custom_image_picker_take_photo), getContext().resources.getString(R.string.custom_image_picker_choose_from_gallery))
        /*AlertManagerUtils.showAlertDialog(
            activity.applicationContext,
            title = getContext().resources.getString(R.string.custom_image_picker_title),
            options = optionItems,
            isCancelable = true,
            optionCallBack = { dialog, item ->
                when (optionItems[item]) {
                    optionItems[0] -> {
                        dialog.dismiss()
                        launchCamera()
                    }
                    optionItems[1] -> {
                        dialog.dismiss()
                        launchGallery()
                    }
                }
            }

        )*/

        val optionsMenu = arrayOf<CharSequence>(
            "Take Photo",
            "Choose from Gallery",
            "Exit"
        ) // create a menuOption Array

        // create a dialog for showing the optionsMenu
        // create a dialog for showing the optionsMenu
        val builder = AlertDialog.Builder(getContext())
        // set the items in builder
        // set the items in builder
        builder.setItems(optionsMenu) { dialogInterface, i ->
            if (optionsMenu[i] == "Take Photo") {
                // Open the camera and get the photo
                launchCamera()
            } else if (optionsMenu[i] == "Choose from Gallery") {
                // choose from  external storage
                launchGallery()
            } else if (optionsMenu[i] == "Exit") {
                dialogInterface.dismiss()
            }
        }
        builder.show()

    }

    /**
     * This is a call back for a specific context
     * /**VERY IMPORTANT THING**/
     * It's important to override onRequestPermissionResult for specific context and just pass
     * those values back to this class we will handle the rest
     * */
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                try {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    saveBitmapToDisk(imageBitmap)
                } catch (err: Exception) {
                    println("Some error happened: ${err.stackTrace}")
                }
            }
            GALLERY_REQUEST_CODE -> {
                val uri = data?.data
                if (uri != null && resultCode == Activity.RESULT_OK) {
                    val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContext().contentResolver, uri))
                    } else {
                        MediaStore.Images.Media.getBitmap(getContext().contentResolver, uri)
                    }
                    saveBitmapToDisk(bitmap)
                }
            }
        }
    }

    /**
     * It's for launching the camera with the custom uri where camera picture will be stored
     * */
    private fun launchCamera() {
        if (PermissionManager.checkPermissionForSet(activity, PermissionManager.Companion.PERMISSION_SETS.IMAGE_PICK)) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityResult(intent, CAMERA_REQUEST_CODE)
        }
    }

    /**
     * It's for launching the gallery  and get the image from there
     * */
    private fun launchGallery() {
        if (PermissionManager.checkPermissionForSet(activity, PermissionManager.Companion.PERMISSION_SETS.IMAGE_PICK)) {
            val pickPhoto =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityResult(pickPhoto, GALLERY_REQUEST_CODE)
        }
    }

    /**
     * This is the entry point for the customImagePicker
     * */
    fun pickImage() {
        fileUtil = FileUtil(getContext())
        imageCompressor = ImageCompressor(fileUtil)
        showImageDialog()
    }
}
