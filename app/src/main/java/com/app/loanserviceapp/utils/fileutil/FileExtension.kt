package com.app.loanserviceapp.utils.fileutil

import android.graphics.Bitmap
import android.util.Base64
import com.diageo.edge.utils.LogUtil
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.lang.Exception

fun File.deleteFile() {
    if (exists()) {
        delete()
    }
}
suspend fun File.writeBitmap(bitmap: Bitmap, format: Bitmap.CompressFormat, quality: Int = 100): Boolean = withContext(Dispatchers.IO) {
    try {
        if (exists()) {
            delete()
        }
        delay(5)
        val bos = ByteArrayOutputStream()
        bitmap.compress(format, quality, bos)
        val bitmapdata: ByteArray = bos.toByteArray()
        outputStream().use { out ->
            out.write(bitmapdata)
            out.flush()
            out.close()
        }
    } catch (err: Exception) {
        LogUtil.debug("I am from error: $err")
        return@withContext false
    }
    return@withContext true
}

fun File.encode(): String {
    return Base64.encodeToString(this.readBytes(), Base64.DEFAULT)
}
