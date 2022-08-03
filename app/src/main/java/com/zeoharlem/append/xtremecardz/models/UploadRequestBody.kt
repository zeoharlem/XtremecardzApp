package com.zeoharlem.append.xtremecardz.models

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.zeoharlem.append.xtremecardz.datasources.ZipRepository
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream

class UploadRequestBody(
    private val file: File,
    private val contentType: String,
    private var uploadFileCallbackListener: UploadFileCallbackListener
): RequestBody() {

    companion object {
        private const val DEFAULT_BUFFER_SIZE = 2048
    }

    private var _progressBarSize = MutableLiveData<Int>(0)
    val progressBarSize get() = _progressBarSize

    interface UploadFileCallbackListener{
        fun uploadCallback(percentage: Int)
    }

    override fun contentType(): MediaType? {
        return "${contentType}/*".toMediaTypeOrNull()
    }

    override fun contentLength(): Long {
        return file.length()
    }

    override fun writeTo(sink: BufferedSink) {
        val length = file.length()
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        val fileInputStream = FileInputStream(file)
        Log.e("UploadFilePath", "writeTo: ${file.absolutePath}", )
        var uploaded = 0L
        try {
            fileInputStream.use { inputStream ->
                var read: Int
                val handler = Handler(Looper.getMainLooper())
                while (inputStream.read(buffer).also {
                        read = it
                        Log.e("writeTo", "writeTo: $read", )
                } != -1) {
                    handler.post(ProgressUpdater(uploaded, length))
                    uploaded += read
                    sink.write(buffer, 0, read)
                    _progressBarSize.value = ((uploaded / length) * 100).toInt()
                    Log.e("progress", "writeTo: ${uploaded}", )

                }
            }
        }
        catch (e: Exception){
            Log.e("ZipRepositoryError", "writeTo: ${e.localizedMessage}", )
        }
        finally {
            Log.e("ZipRepository", "writeTo: Completed", )
            fileInputStream.close()
        }
    }

    inner class ProgressUpdater(
        private val uploaded: Long,
        private val total: Long,
    ) : Runnable {
        override fun run() {
            Log.e("ZipRepositoryError", "writeTo: Called ProgressUpdater", )
            uploadFileCallbackListener.uploadCallback((100 * uploaded / total).toInt())
        }
    }

    fun setUploadFileCallbackListener(callbackListener: UploadFileCallbackListener){
        uploadFileCallbackListener = callbackListener
    }
}