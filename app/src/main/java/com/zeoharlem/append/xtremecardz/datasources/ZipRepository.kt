package com.zeoharlem.append.xtremecardz.datasources

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.zeoharlem.append.xtremecardz.models.CapturedImage
import com.zeoharlem.append.xtremecardz.sealed.NetworkResults
import com.zeoharlem.append.xtremecardz.services.XtremecardzServices
import com.zeoharlem.append.xtremecardz.utils.Constants.Companion.BUFFER
import com.zeoharlem.append.xtremecardz.utils.XtremeCardzUtils.getFileName
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okio.BufferedSink
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import javax.inject.Inject

class ZipRepository @Inject constructor(private val xtremecardzServices: XtremecardzServices) {

    fun createZipDirectory(context: Context,folder: String, fileCollection: ArrayList<CapturedImage>): String {
        val listStringFiles = arrayListOf<String>()
        try{
            val storeDirectory = context.getExternalFilesDir(Environment.DIRECTORY_DCIM)
            val outputFile = File(storeDirectory, folder+ File.separator)
            outputFile.mkdir()
            for(uriFile in fileCollection){
                copyFileEvent(context, uriFile.capturedItem!!, outputFile)
            }
            for(listFile in outputFile.listFiles()!!){
                listStringFiles.add(listFile.absolutePath)
                Log.e("UploadBottom", "createZipDirectory Paths: ${listFile.absolutePath}")
            }
            zipFolder(listStringFiles, outputFile.absolutePath+".zip")
            return outputFile.absolutePath+".zip"
        }
        catch (e: Exception){
            Log.e("UploadBottomError", "createZipDirectory: ${e.localizedMessage}")
            return ""
        }
    }

    //Upload File selected
    suspend fun uploadFile(token: String, query: HashMap<String, RequestBody>, file: MultipartBody.Part): ResponseBody {
        return xtremecardzServices.uploadFile(token, query, file)
    }

    //Copy each file into the folder
    private fun copyFileEvent(context: Context, sourceFilePath: Uri, destinationFile: File) {
        val fileName = context.getFileName(sourceFilePath)
        //val fileExtension = XtremeCardzUtils.getFileExtension(requireContext(), sourceFilePath)
        val tempFile = File(destinationFile, fileName!!)
        tempFile.createNewFile()
        try{
            val outputStream = FileOutputStream(tempFile)
            val inputStream = context.contentResolver.openInputStream(sourceFilePath)
            inputStream?.let {
                flatCopy(inputStream, outputStream)
            }
            outputStream.flush()
        }
        catch (e: Exception){
            Log.e("UploadBottomError", "copyFileEventError: ${e.localizedMessage}")
        }
    }

    //Copy read file to copy
    private fun flatCopy(source: InputStream, target: OutputStream){
        val buffer = ByteArray(8192)
        var length: Int
        while (source.read(buffer).also { length = it } > 0){
            target.write(buffer, 0, length)
        }
    }

    //zip a folder with contents
    private fun zipFolder(files: ArrayList<String>, zipFile: String) {
        var origin: BufferedInputStream? = null
        val out = ZipOutputStream(BufferedOutputStream(FileOutputStream(zipFile)))
        try {
            val data = ByteArray(BUFFER)
            for (i in files.indices) {
                val fi = FileInputStream(files[i])
                origin = BufferedInputStream(fi, BUFFER)
                try {
                    val entry = ZipEntry(files[i].substring(files[i].lastIndexOf("/") + 1))
                    out.putNextEntry(entry)
                    var count: Int
                    while (origin.read(data, 0, BUFFER).also { count = it } != -1) {
                        out.write(data, 0, count)
                    }
                }
                catch (e: Exception){
                    Log.e("ZipRepositoryError", "zipFolder: ${e.localizedMessage}", )
                }
                finally {
                    origin.close()
                }
            }
        }
        catch (e: Exception){
            Log.e("ZipRepositoryError", "zipFolder: ${e.localizedMessage}", )
        }
        finally {
            out.close()
        }
    }


}