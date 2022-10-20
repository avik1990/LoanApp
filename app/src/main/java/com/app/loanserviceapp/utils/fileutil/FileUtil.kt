package com.app.loanserviceapp.utils.fileutil

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Environment.*
import android.os.StatFs
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.app.loanserviceapp.utils.LogUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okio.*
import java.io.File
import java.io.IOException
import java.io.InputStream


class FileUtil constructor(private val context: Context) {

    companion object {
        const val LEADER_BOARD_FOLDER = "LeaderboardImageThumbnail"
        const val OUTLET_THUMBNAIL_FOLDER = "OutletThumbnail"
        const val OPPOTUNITY_TEMPLATE_FOLDER = "OpportunityTemplateThumbnails"
        const val PROMOTION = "Promotion"
        const val LEADERBOARDTHUMBNAIL = "LeaderboardImageThumbnail"
        const val EXTERNALLINKSICONS = "ExternalLinksIcons"
        const val CHATTER_NOTE_THUMBNIL = "LoanServices"
        const val OPPORTUNITY_TEMPLATE = "OpportunityTemplateThumbnails"
        const val OUTLET_SURVEY_FOLDER = "OutletSurveyFolder"
        const val COACHING_REPORT_MANAGER = "CoachingReportManagerFolder"
        const val SUPPORT_REQUEST_THUMBNAIL = "SupportRequestThumbnail"

    }
    val directoryPath = "$DIRECTORY_DOCUMENTS/LoanServices/"



    fun getBitmapFromFile(storageDir: String, filename: String): Bitmap {
        val fileUri = FileProvider.getUriForFile(context, context.packageName + ".provider", File(getFileUri(storageDir, filename)))
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, fileUri))
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, fileUri)
        }
    }

    fun getBitmapFromFile(file: File): Bitmap {
        return BitmapFactory.decodeFile(file.absolutePath)
    }

    fun getFileUri(storageDir: String, filename: String): String {
        // If External SD Card Available
        if (isExternalMemoryAvailable()) {
            getExternalDirectory(storageDir).let { externalPath ->
                return ("$externalPath/$filename")
            }
        }
        // If External Storage  not available but internal storage Availabe
        else if (isExternalStorageAvailable() && !isExternalStorageReadOnly()) {
            getExternalFileDirectory(storageDir).let { externalPath ->
                return ("$externalPath/$filename")
            }
        }
        // If external or internal storage not available
        else {
            getDataDirectory().let { file ->
                return ("${file.absolutePath}/$filename")
            }
        }
    }

    fun saveImage(storageDir: String, filename: String, bitmap: Bitmap) {
        // If External SD Card Available
        if (isExternalMemoryAvailable()) {
            getExternalDirectory(storageDir).let { externalPath ->
                createBitmapFile("$externalPath/$filename", "$externalPath", bitmap)
            }
        }
        // If External Storage  not available but internal storage Availabe
        else if (isExternalStorageAvailable() && !isExternalStorageReadOnly()) {
            getExternalFileDirectory(storageDir).let { externalPath ->
                createBitmapFile("$externalPath/$filename", "$externalPath", bitmap)
            }
        }
        // If external or internal storage not available
        else {
            getDataDirectory().let { file ->
                createBitmapFile("${file.absolutePath}/$filename", file.absolutePath, bitmap)
            }
        }
    }

    /**
     * Storage Volume -> Get Available free Space
     */
    fun getStorageVolume(storageDir: String): Long? {
        // If External SD Card Available
        if (isExternalMemoryAvailable()) {
            getExternalDirectory(storageDir).let { externalPath ->
                return getFreeSpaceFromDisk(externalPath)
            }
        }
        // If External Storage  not available but internal storage Availabe
        else if (isExternalStorageAvailable() && !isExternalStorageReadOnly()) {
            getExternalDirectory(storageDir).let { externalPath ->
                return getFreeSpaceFromDisk(externalPath)
            }
        }
        // If external or internal storage not available
        else {
            getDataDirectory().let { file ->
                return getFreeSpaceFromDisk(file.absolutePath)
            }
        }
    }

    /**
     * Return Free Space
     */
    private fun getFreeSpaceFromDisk(externalPath: String?): Long {
        val stat = StatFs(externalPath)
        val bytesAvailable: Long = stat.blockSizeLong * stat.availableBlocksLong
        return bytesAvailable / (1024 * 1024 * 1024)
    }

    /**
     * @param storageDir
     * @param filename
     *@param data String
     * Used to Save Text file in SD card if available
     * or will store to device Internal Storage or to data directory
     */
    fun saveTextFile(storageDir: String, filename: String, data: String) {
        // If External SD Card Available
        if (isExternalMemoryAvailable()) {
            getExternalDirectory(storageDir).let { externalPath ->
                writeTextFile("$externalPath/$filename", "$externalPath", data)
            }
        }
        // If External Storage  not available but internal storage Availabe
        else if (isExternalStorageAvailable() && !isExternalStorageReadOnly()) {
            getExternalFileDirectory(storageDir).let { externalPath ->
                writeTextFile("$externalPath/$filename", "$externalPath", data)
            }
        }
        // If external or internal storage not available
        else {
            getDataDirectory().let { file ->
                writeTextFile("${file.absolutePath}/$filename", file.absolutePath, data)
            }
        }
    }

    /**
     * @param storageDir
     * @param filename
     *@param data String
     * Used to Save Text file in SD card if available
     * or will store to device Internal Storage or to data directory
     */
    fun saveTextFile(storageDir: String, filename: String, inputStream: InputStream) {
        // If External SD Card Available
        if (isExternalMemoryAvailable()) {
            getExternalDirectory(storageDir).let { externalPath ->
                writeTextFile("$externalPath/$filename", "$externalPath", inputStream)
            }
        }
        // If External Storage  not available but internal storage Availabe
        else if (isExternalStorageAvailable() && !isExternalStorageReadOnly()) {
            getExternalFileDirectory(storageDir).let { externalPath ->
                writeTextFile("$externalPath/$filename", "$externalPath", inputStream)
            }
        }
        // If external or internal storage not available
        else {
            getDataDirectory().let { file ->
                writeTextFile("${file.absolutePath}/$filename", file.absolutePath, inputStream)
            }
        }
    }

    private fun getFileReference(storageDir: String, filename: String): File? {
        return if (isExternalMemoryAvailable()) {
            getExternalDirectory(storageDir)?.let { externalPath ->
                File("$externalPath/$filename")
            }
        }
        // If External Storage  not available but internal storage Available
        else if (isExternalStorageAvailable() && !isExternalStorageReadOnly()) {
            getExternalFileDirectory(storageDir)?.let { externalPath ->
                File("$externalPath/$filename")
            }
        } else {
            getDataDirectory()?.let { file ->
                File("${file.absolutePath}/$filename")
            }
        }
    }
    private fun getFileDirectoryReference(storageDir: String): File? {
        return if (isExternalMemoryAvailable()) {
            getExternalDirectory(storageDir)?.let { externalPath ->
                File(externalPath)
            }
        }
        // If External Storage  not available but internal storage Availabe
        else if (isExternalStorageAvailable() && !isExternalStorageReadOnly()) {
            getExternalFileDirectory(storageDir)?.let { externalPath ->
                File("$externalPath")
            }
        } else {
            getDataDirectory()?.let { file ->
                File(file.absolutePath)
            }
        }
    }
    /**
     * @param storageDir
     * @param filename
     *@param source  Okio Source
     * Used to Save Text file in SD card if available
     * or will store to device Internal Storage or to data directory
     */
    fun saveTextFile(storageDir: String, filename: String, source: InputStream, isAppenable: Boolean): Boolean {
        // If External SD Card Available
        if (isExternalMemoryAvailable()) {
            getExternalDirectory(storageDir).let { externalPath ->
                return writeTextFile("$externalPath/$filename", "$externalPath", source, isAppenable)
            }
        }
        // If External Storage  not available but internal storage Availabe
        else if (isExternalStorageAvailable() && !isExternalStorageReadOnly()) {
            getExternalFileDirectory(storageDir).let { externalPath ->
                return writeTextFile("$externalPath/$filename", "$externalPath", source, isAppenable)
            }
        }
        // If external or internal storage not available
        else {
            getDataDirectory().let { file ->
                return writeTextFile("${file.absolutePath}/$filename", file.absolutePath, source, isAppenable)
            }
        }
    }
    /**
     * Will Write Text to specific path defined
     * @param dir -> Directory Name
     * @param filePath -> Full filepath
     */
    private fun writeTextFile(filePath: String, dir: String, textData: String): Boolean {
        if (!isFileOrDirAlreadyPresent(dir)) {
            return createDirectory(dir)
        }
        if (!isFileOrDirAlreadyPresent(filePath)) {
            // File  Not Present
            LogUtil.debug("=======> New File Created")
            return createFile(filePath, textData)
        } else {
            LogUtil.debug("=======> File Already Present So Append")
            return appendFile(filePath, textData)
        }
    }
    /**
     * Will Write Text to specific path defined
     * @param dir -> Directory Name
     * @param filePath -> Full filepath
     */
    private fun writeTextFile(filePath: String, dir: String, inputStream: InputStream): Boolean {
        if (!isFileOrDirAlreadyPresent(dir)) {
            return createDirectory(dir)
        }
        if (!isFileOrDirAlreadyPresent(filePath)) {
            // File  Not Present
            LogUtil.debug("=======> New File Created")
            return createFile(filePath, inputStream)
        } else {
            LogUtil.debug("=======> File Already Present So Append")
            return appendFile(filePath, inputStream)
        }
    }

    /**
     * Will Write Text to specific path defined
     * @param dir -> Directory Name
     * @param filePath -> Full filepath
     * @param source Source(Okio)
     */
    private fun writeTextFile(filePath: String, dir: String, source: InputStream, isAppenable: Boolean): Boolean {
        if (!isFileOrDirAlreadyPresent(dir)) {
            return createDirectory(dir)
        }
        if (!isFileOrDirAlreadyPresent(filePath)) {
            // File  Not Present
            return createFile(filePath, source)
        } else {
            if (isAppenable) {
                return appendFile(filePath, source)
            } else {
                deleteDirectoryImpl(filePath)
                return createFile(filePath, source)
            }
        }
    }

    /**
     * REceiver responsible for closing the stream
     */
    fun readFileViaSource(filename: File?): BufferedSource? {
        return if (filename != null) filename.inputStream().source().buffer() else null
    }
    /**
     * Get All File name from a directory
     */
    fun getAllFileFromDirectory(directoryName: String): Array<File>? {
        val directoryPath = getFileDirectoryReference(directoryName)
        return directoryPath?.listFiles()
    }
    /**
     * Get All File name from a directory
     */
    fun getAllFile(directoryName: String): FileTreeWalk? {
        val directoryPath = getFileDirectoryReference(directoryName)
        return directoryPath?.walkTopDown()
    }

    /**
     * Read Text using buffered Reader
     * storage Dir -> Directory From Where the data will be read
     * filename -> Name of the file from where the content need to be read
     */

    fun readText(storageDir: String, filename: String): List<String>? {
        try {
            if (isExternalMemoryAvailable()) {
                getExternalDirectory(storageDir).let { externalPath ->
                    return File("$externalPath/$filename").bufferedReader().readLines()
                }
            }
            // If External Storage  not available but internal storage Availabe
            else if (isExternalStorageAvailable() && !isExternalStorageReadOnly()) {
                getExternalFileDirectory(storageDir).let { externalPath ->
                    return File("$externalPath/$filename").bufferedReader().readLines()
                }
            }
            // If external or internal storage not available
            else {
                getDataDirectory().let { file ->
                    return File("${file.absolutePath}/$filename").bufferedReader().readLines()
                }
            }
        } catch (e: IOException) {
            return null
        }
    }

    fun renameFile(storageDir: String, fromFileName: String, toFileName: String) {
        val from = getFile(storageDir, fromFileName)
        val to = getFileReference(storageDir, toFileName)
        from?.let { fromFile ->
            to?.let { toFile ->
                if (fromFile.exists()) from.renameTo(toFile)
            }
        }
    }

    /**
     * Return File if present or return null if file not present
     * @param storageDir - File Dir name
     * @param filename - File name
     * @return File
     * @author Sumit chakraborty
     */
    fun getFile(storageDir: String, filename: String): File? {
        try {
            if (isExternalMemoryAvailable()) {
                getExternalDirectory(storageDir).let { externalPath ->
                    return if (isFileOrDirAlreadyPresent("$externalPath/$filename")) { File("$externalPath/$filename") } else null
                }
            }
            // If External Storage  not available but internal storage Available
            else if (isExternalStorageAvailable() && !isExternalStorageReadOnly()) {
                getExternalFileDirectory(storageDir).let { externalPath ->
                    return if (isFileOrDirAlreadyPresent("$externalPath/$filename")) { File("$externalPath/$filename") } else null
                }
            }
            // If external or internal storage not available
            else {
                getDataDirectory().let { file ->
                    return if (isFileOrDirAlreadyPresent("${file.absolutePath}/$filename")) { File("${file.absolutePath}/$filename") } else null
                }
            }
        } catch (e: IOException) {
            return null
        }
    }
    /**
     * Delete the directory and all sub content.
     *
     * @param path The absolute directory path. For example:
     * *mnt/sdcard/NewFolder/ *.
     * @return `True` if the directory was deleted, otherwise return
     * `False`
     */
    fun deleteDirectoryImpl(storageDir: String) {
        // If External SD Card Available
        if (isExternalMemoryAvailable()) {
            getExternalDirectory(storageDir).let { externalPath ->
                File(externalPath).deleteRecursively()
            }
        }
        // If External Storage  not available but internal storage Availabe
        else if (isExternalStorageAvailable() && !isExternalStorageReadOnly()) {
            getExternalFileDirectory(storageDir).let { externalPath ->
                externalPath?.deleteRecursively()
            }
        }
        // If external or internal storage not available
        else {
            getDataDirectory().let { file ->
                file?.deleteRecursively()
            }
        }
    }

    // Is External Storage Available
    private fun isExternalStorageAvailable(): Boolean {
        return getExternalStorageState() == MEDIA_MOUNTED
    }
    // Only Read Only Device
    private fun isExternalStorageReadOnly(): Boolean {
        return getExternalStorageState() == MEDIA_MOUNTED_READ_ONLY
    }
    // Is SD Card Available
    private fun isExternalMemoryAvailable(): Boolean {
        val storage: Array<out File>? = context.getExternalFilesDirs("$DIRECTORY_DOCUMENTS/Diageo/")
        storage?.let { storageSize ->
            if (storageSize.size > 1) {
                return true
            }
        }
        return false
    }
    // get SD card Path  if Available
    private fun getExternalDirectory(folderName: String): String? {
        val storage: Array<out File>? = context.getExternalFilesDirs("$DIRECTORY_DOCUMENTS/Diageo/$folderName")
        storage?.let { storageSize ->
            return when {
                storageSize.size > 1 -> {
                    storage[1].absolutePath
                }
                storageSize.size == 1 -> {
                    storage[0].absolutePath
                }
                else -> {
                    null
                }
            }
        }
        return null
    }
    // Get External File Directory
    private fun getExternalFileDirectory(folderName: String): File? {
        return context.getExternalFilesDir("$DIRECTORY_DOCUMENTS/Diageo/$folderName")?.absoluteFile
    }
    // Get All FIle Directory
    private fun getAllExternalFileDirectory(folderName: String): Array<out File>? {
        return context.getExternalFilesDirs("$DIRECTORY_DOCUMENTS/Diageo/$folderName")
    }

    // Check if Directory Already present
    private fun isFileOrDirAlreadyPresent(path: String): Boolean {
        LogUtil.debug("===================>> FilePresent $path ==== is file preset \n ${File(path).exists()} ")
        return File(path).exists()
    }

    // Create Directory
    private fun createDirectory(path: String): Boolean {
        return File(path).mkdirs()
    }

    /**
     * Save Bitmap to local Storage
     * @param filePath
     * @param bitmap file
     * @return is created Successfully (Boolean)
     */
    private fun createBitmapFile(filePath: String, filename: String, bitmap: Bitmap): Boolean {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                File(filePath, filename).writeBitmap(bitmap, Bitmap.CompressFormat.PNG, 100)
            }
        } catch (e: IOException) {
            return false
        }
        return true
    }

    /**
     * Create File
     * @param filePath
     * @param content
     * @return is created Successfully (Boolean)
     */
    private fun createFile(filePath: String, content: ByteArray): Boolean {
        try {
            File(filePath).outputStream().write(content)
        } catch (e: IOException) {
            return false
        }
        return true
    }

    /**
     * Create File
     * @params path
     * @Return Byte Array
     */
    private fun createFile(path: String, content: String): Boolean {
        try {
            File(path).bufferedWriter().use { out ->
                out.write(content)
            }
        } catch (e: IOException) {
            return false
        }
        return true
    }

    /**
     * Create File
     * @params path
     * @Return source:  Source
     * This function takes Okio source as input and writes it in a file
     */
    private fun createFile(path: String, source: InputStream): Boolean {
        val bufferedSource: BufferedSource = source.source().buffer()
        var bufferedSink: BufferedSink? = null
        try {
            val fileSink: Sink = File(path).sink()
            bufferedSink = fileSink.buffer()
            if (bufferedSink.isOpen) {
                bufferedSink.writeAll(bufferedSource)
                bufferedSink.close()
            } else {
                LogUtil.error("=====>BufferSink Channel is Closed == Please Try Again!!!")
            }
        } catch (e: IOException) {
            return false
        } finally {
            bufferedSink?.close()
            bufferedSource.close()
        }
        return true
    }
    /**
     * append File
     * @params path
     * @Return source:  Source
     * This function takes Okio source as input, appends and writes it in a file
     */
    private fun appendFile(path: String, source: InputStream): Boolean {
        val bufferedSource: BufferedSource = source.source().buffer()
        var bufferedSink: BufferedSink? = null
        try {
            val fileSink: Sink = File(path).appendingSink()
            bufferedSink = fileSink.buffer()
            if (bufferedSink.isOpen) {
                bufferedSink.writeAll(bufferedSource)
                bufferedSink.close()
            }
        } catch (e: IOException) {
            return false
        } finally {
            bufferedSink?.close()
            bufferedSource.close()
        }
        return true
    }

    private fun appendFile(path: String, content: String): Boolean {
        return appendFile(path, content.toByteArray())
    }

    fun deleteFile(filePath: String) {
        val file = File(filePath)
        LogUtil.debug("FILE_PATH:$filePath")
        if (file.exists()) {
            LogUtil.debug("FILE DELETED:${file.deleteRecursively()}")
        }
    }

    private fun appendFile(path: String, bytes: ByteArray?): Boolean {
        if (!isFileOrDirAlreadyPresent(path)) {
            LogUtil.warning("===========Impossible to append content, because such file doesn't exist")
            return false
        }
        try {
            bytes?.let { dataBytes -> File(path).appendBytes(dataBytes) }
            return true
        } catch (e: IOException) {
            LogUtil.warning("Failed to append content to file $e")
            return false
        }
    }
}
