package com.shamardn.podcasttime.util

import android.content.Context
import android.os.Environment
import androidx.core.content.ContextCompat
import java.io.File

object FileUtils {
    fun getRootDirPath(context: Context): String {
        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            val file: File = ContextCompat.getExternalFilesDirs(
                context.applicationContext,
                null
            )[0]
            file.absolutePath
        } else {
            context.applicationContext.filesDir.absolutePath
        }
    }

//    fun downloadMp3UsingUrl(context: Context, url: String, dirPath: String, fileName: String) {
//        PRDownloader.initialize(context.applicationContext)
//        PRDownloader.download(
//            url,
//            dirPath,
//            fileName
//        ).build()
//            .setOnProgressListener { progress ->
//                // Handle download progress if needed
//            }
//            .start(object : OnDownloadListener {
//                override fun onDownloadComplete() {
//                    Toast.makeText(context.applicationContext, "$fileName download Complete", Toast.LENGTH_LONG).show()
//                }
//
//
//                override fun onError(error: com.downloader.Error?) {
//                    Toast.makeText(
//                        context.applicationContext,
//                        "Error in downloading file : $error",
//                        Toast.LENGTH_LONG
//                    )
//                        .show()
//                }
//            })
//    }

}