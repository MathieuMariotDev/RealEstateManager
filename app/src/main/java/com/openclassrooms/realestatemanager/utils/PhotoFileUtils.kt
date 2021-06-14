package com.openclassrooms.realestatemanager.utils

import android.app.usage.ExternalStorageStats
import android.os.Environment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object PhotoFileUtils {

    fun createImageFile(storageDir : File?): File {
        val timeStamp =
            SimpleDateFormat.getDateTimeInstance().format(Date()).replace(":", "").replace("?", "")

        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }
}