package com.zeoharlem.append.xtremecardz.models

import android.util.Log
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream

class ProgressEmittingRequestBody (private val mediaType: String, val file: File) : RequestBody() {

    //private val progressSubject = PublishSubject.create<Int>()

    override fun contentType(): MediaType? = "${mediaType}/*".toMediaTypeOrNull()

    override fun contentLength(): Long {
        return file.length()
    }

    override fun writeTo(sink: BufferedSink) {
        val inputStream = FileInputStream(file)
        val buffer = ByteArray(BUFFER_SIZE)
        var uploaded: Long = 0
        val fileSize = file.length()

        try {
            while (true) {
                val read = inputStream.read(buffer)
                if (read == -1) break
                uploaded += read
                sink.write(buffer, 0, read)
                val progress = (((uploaded / fileSize.toDouble())) * 100).toInt()
                Log.e("ProgressEmitting", "writeTo: $progress", )
                //progressSubject.onNext(progress)
            }

        } catch (e: Exception) {
            e.printStackTrace()
            //progressSubject.onError(e)
        } finally {
            inputStream.close()
        }
    }
    companion object {
        const val BUFFER_SIZE = 1024
    }
}