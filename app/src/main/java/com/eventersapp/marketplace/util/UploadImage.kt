package com.eventersapp.marketplace.util

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import io.tus.java.client.ProtocolException
import io.tus.java.client.TusClient
import io.tus.java.client.TusExecutor
import io.tus.java.client.TusUpload
import java.io.File
import java.io.IOException


class UploadImage(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    companion object {
        const val URL = "http://tus.eventersapp.com/upload/"
    }

    override suspend fun doWork(): Result {
        val filePath = inputData.getString(AppConstants.ARG_FILE_PATH)
        val mediaPath = inputData.getString(AppConstants.ARG_MEDIA_PATH)
        val mediaName = inputData.getString(AppConstants.ARG_MEDIA_NAME)

        return try {

            val client = TusClient()
            val map = HashMap<String, String>()
            map["Token-ID"] = ""
            map["Media-Path"] = mediaPath ?: ""
            map["Media-Name"] = mediaName ?: ""
            client.headers = map
            client.uploadCreationURL = java.net.URL(URL)
            val file = File(filePath)
            val upload = TusUpload(file)
            Log.i("Info", "Starting upload...")
            val executor: TusExecutor = object : TusExecutor() {
                @Throws(ProtocolException::class, IOException::class)
                override fun makeAttempt() {
                    val uploader = client.resumeOrCreateUpload(upload)
                    uploader.chunkSize = 1024
                    do {
                        val totalBytes = upload.size
                        val bytesUploaded = uploader.offset
                        val progress = bytesUploaded.toDouble() / totalBytes * 100
                        //Log.i("Info", "Upload at $progress")
                    } while (uploader.uploadChunk() > -1)

                    uploader.finish()
                    Log.i("Info", "Upload finished.")
                    Log.i("Info", "Upload available at: ${uploader.uploadURL} ")
                }
            }
            executor.makeAttempts()

            Log.i("Info", "Work Finish")
            Result.success()
        } catch (error: Throwable) {
            Log.e("Error", "Error ${error.message}}")
            Result.failure()
        }
    }
}
