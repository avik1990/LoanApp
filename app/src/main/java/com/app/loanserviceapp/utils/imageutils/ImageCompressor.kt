package com.app.loanserviceapp.utils.imageutils

import android.graphics.Bitmap
import com.app.loanserviceapp.utils.fileutil.FileUtil
import com.app.loanserviceapp.utils.fileutil.writeBitmap
import com.app.loanserviceapp.utils.roundOffDecimalCellingTwoDecimal
import com.diageo.edge.utils.LogUtil
import com.squareup.picasso.Picasso

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

class ImageCompressor  constructor(private val fileUtil: FileUtil) {

    companion object {
        enum class SIZE_TYPE {
            GB,
            MB,
            KB
        }
    }

    private fun calculateImageSize(size: Int = 1, sizeType: SIZE_TYPE = SIZE_TYPE.MB): Long {
        return when (sizeType) {
            SIZE_TYPE.GB -> size.times(1000000000).toLong()
            SIZE_TYPE.MB -> size.times(1000000).toLong()
            SIZE_TYPE.KB -> size.times(1000).toLong()
        }
    }
    /**
     * By default 1 MB is the maximum size that you can resize it
     * else you can also modify this by passing it to argument
     * @param image Image Bitmap for compression
     * @param factor By which factor you want to decrease resolution
     * @param imageSize(in MB) By which size you want to decrease it to
     * @param listener It is for the callback to the one which user can specify to get the compressed bitmap
     * @see reduceBitmapSize
     * */
    fun resizeImage(
        image: Bitmap,
        factor: Int = 10,
        imageSize: Int = 1,
        sizeType: SIZE_TYPE = SIZE_TYPE.MB,
        listener: (bitmap: Bitmap) -> Unit
    ) {

        val width = image.width
        val height = image.height

        val scaleWidth = width / factor
        val scaleHeight = height / factor

        if (scaleHeight == 0 || scaleWidth == 0 || image.byteCount <= calculateImageSize(imageSize, sizeType)) {
            listener(image)
            return
        }

        return resizeImage(Bitmap.createScaledBitmap(image, scaleWidth, scaleHeight, false), listener = listener)
    }

    /**
     * Reducing the size of the image maintaining aspect ratio
     * @param filePath File path of the local file system to resize the image
     * @param width Width of the final resized image
     * @param height height of the resized image
     * */
    fun reduceSize(filePath: String, width: Int, height: Int): Bitmap {
        return Picasso
            .get()
            .load(File(filePath))
            .resize(width, height)
            .centerInside()
            .onlyScaleDown()
            .get()
    }

    /**
     *Compress single file and compress it and save it ti the destination
     * @param filePath file path for compressing the file (Target path of the file)
     * @param destinationPath it's optional. If it is not present then the bitmap will be saved to the original location
     * @param height Height of the resized image
     * @param width Width of the resized image
     */
    suspend fun compressFile(filePath: String, destinationPath: String? = null, height: Int, width: Int): Boolean {
        var destination = filePath
        val isWritten: Boolean
        destinationPath?.let {
            destination = it
        }
        val bitmap = reduceSize(filePath, width, height)
        val file = File(destination)
        isWritten = file.writeBitmap(bitmap = bitmap, format = Bitmap.CompressFormat.JPEG)
        LogUtil.debug("FILE IS WRITTEN OR NOT..$isWritten && $destination")
        return isWritten
    }

    /**
     * Compress list of files and save it to a separate folder or can save the file into itself
     * @param filePaths list of file paths that can be compressed
     * @param destinationFolder Destination folder where to store the compressed files
     * @param height height of the image for resizing
     * @param width Width of the image to resize
     * */
    fun compressFiles(filePaths: List<String>, destinationFolder: String? = null, height: Int, width: Int): Flow<String> {
        return flow {
            for (filepath in filePaths) {
                val fullName = filepath.substringAfterLast("/")
                val fileName = fullName.substringBeforeLast(".")
                val extension = fullName.substringAfterLast(".")
                var destinationPath = filepath
                if (destinationFolder != null) {
                    val destinationFilePath = fileUtil.getFileUri(destinationFolder, "$fileName.$extension")
                    destinationPath = destinationFilePath
                    compressFile(filepath, destinationFilePath, height = height, width = width)
                } else {
                    compressFile(filepath, height = height, width = width)
                }
                emit(destinationPath)
            }
        }
    }

    /**
     * Overloaded function for compress bitmap and save to destination
     * @param bitmap Bitmap to compress
     * @param destinationFileUri Destination file uri to save the compressed image
     * @param callBack Lambda function to populate the thing compressed bitmap and file uri where the compressed bitmap is being saved
     * */
    fun compressBitmapAndSaveToFile(
        bitmap: Bitmap,
        destinationFileUri: String,
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG,
        callBack: (bitmap: Bitmap, fileUri: String, percentage: Float) -> Unit
    ) {
        resizeImage(bitmap) {
            CoroutineScope(Dispatchers.IO).launch {
                val isWritten = File(destinationFileUri).writeBitmap(it, format)
                val percentage = it.byteCount.toFloat().div(bitmap.byteCount.toFloat()).times(100f).roundOffDecimalCellingTwoDecimal()
                if (isWritten) callBack(it, destinationFileUri, percentage)
            }
        }
    }

    /**
     * Overloaded function for compress bitmap and save to destination
     * @param bitmap Bitmap to compress
     * @param folder Folder name where to save file
     * @param fileName Filename of the image where to save the bitmap
     * @param height File height for the resize image
     * @param callBack Callback for provide user the reduced bitmap and destination file uri
     * */
    fun compressBitmapAndSaveToFile(
        bitmap: Bitmap,
        folder: String,
        fileName: String,
        height: Int,
        callBack: (bitmap: Bitmap, fileUri: String, percentage: Float) -> Unit
    ) {
        val fileUri = fileUtil.getFileUri(folder, fileName)
        compressBitmapAndSaveToFile(bitmap, fileUri, callBack = callBack)
    }


}
