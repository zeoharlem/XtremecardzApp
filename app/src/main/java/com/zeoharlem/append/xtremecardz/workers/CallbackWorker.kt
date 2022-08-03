package com.zeoharlem.append.xtremecardz.workers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.camera.core.impl.utils.futures.Futures
import androidx.concurrent.futures.CallbackToFutureAdapter
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.google.common.util.concurrent.ListenableFuture
import com.zeoharlem.append.xtremecardz.R
import com.zeoharlem.append.xtremecardz.models.UploadRequestBody
import com.zeoharlem.append.xtremecardz.notifications.MyAppsNotificationManager
import com.zeoharlem.append.xtremecardz.services.MyApi
import com.zeoharlem.append.xtremecardz.utils.Constants
import com.zeoharlem.append.xtremecardz.utils.XtremeCardzUtils
import com.zeoharlem.append.xtremecardz.utils.XtremeCardzUtils.getFileName
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class CallbackWorker(
    val context: Context,
    params: WorkerParameters
) : ListenableWorker(context, params) {

    private var requestBody: UploadRequestBody? = null
    private lateinit var notificationManager: NotificationManager
    private val myAppsNotificationManager = MyAppsNotificationManager(context)
    private var outputData: Data? = null

    override fun startWork(): ListenableFuture<Result> {

        return CallbackToFutureAdapter.getFuture { completer ->
            if(inputData.getString("type")!! != "multiple") {
                val file = File(inputData.getString("files")!!)
                val token = XtremeCardzUtils.readKey("token", context)
                val callback = object : Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        completer.setException(t)
                    }

                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        response.body()?.let {
                            completer.set(Result.success(workDataOf(
                                "networkResult" to it.string())))
                        }
                    }
                }
                uploadCreatedFile("Bearer $token", Uri.fromFile(file), callback)
                callback
            }
            else{
                val filesString = inputData.getString("files")!!
                val token = XtremeCardzUtils.readKey("token", context)
                Log.e("CallbackWorker", "startWork: $filesString", )

                val callback = object : Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        completer.setException(t)
                    }

                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        response.body()?.let {
                            completer.set(Result.success(workDataOf(
                                "networkResult" to it.string())))
                        }
                    }
                }
                multipleImageUpload("Bearer $token", filesString, callback)
                callback
            }
        }
    }

    override fun getForegroundInfoAsync(): ListenableFuture<ForegroundInfo> {
        return Futures.immediateFuture(ForegroundInfo(
            Constants.NOTIFICATION_ID, createNotification(20)
        ))
    }

    /**
     * Upload Multiple Images<ArrayList<Images>>
     */
    private fun multipleImageUpload(
        token: String,
        selectedImagesUri: String, callback: Callback<ResponseBody>)
    {
        val splitByCommaImageFile = selectedImagesUri.split(",")
        val selectedImagesMultipartBody = mutableListOf<MultipartBody.Part>()
        Log.e("MyZipWorker", "onResponse: called numbers ${splitByCommaImageFile.size}")

        for((key, imageFile) in splitByCommaImageFile.withIndex()){
            val file = File(imageFile)
            Log.e("MyZipUploadFile", "filename: ${file.name}")
            Log.e("MyZipUploadFile", "absolutePath: ${file.absolutePath}")

            val requestBody: RequestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val propertyImagePart: MultipartBody.Part   = MultipartBody.Part.createFormData(
                "images", file.name, requestBody
            )
            selectedImagesMultipartBody.add(propertyImagePart)
        }
        MyApi().uploadMultipleImage(
            token,
            selectedImagesMultipartBody,
            requestBodyQueries()
        ).enqueue(callback)
    }

    /**
     * Upload single file Zipped with collections of files
     * already Zipped file selection
     */
    private fun uploadCreatedFile(
        token: String,
        selectedFileUri: Uri,
        callback: Callback<ResponseBody>
    ){
        val parcelFileDescriptor = context.contentResolver.openFileDescriptor(
            selectedFileUri, "r", null)

        val inputStream = FileInputStream(parcelFileDescriptor!!.fileDescriptor)
        val file = File(context.cacheDir, context.getFileName(selectedFileUri)!!)
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        Log.e("UploadCreatedFile", "uploadCallback: wall called")

        requestBody = UploadRequestBody(
            file,
            "application",
            object : UploadRequestBody.UploadFileCallbackListener {
                override fun uploadCallback(percentage: Int) {
                    Log.e("UploadPercentage", "uploadCallback: $percentage")
                }
            }
        )

        MyApi().uploadImage(
            token,
            MultipartBody.Part.createFormData(
                "xtreme_file",
                file.name,
                requestBody!!
            ),
            requestBodyQueries()
        ).enqueue(callback)
    }

    suspend fun getForegroundInfo(): ForegroundInfo {
        val notification = createNotification(100)
        return ForegroundInfo(Constants.NOTIFICATION_ID, notification)
    }

    private fun createNotification(downloadProgress: Int): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                Constants.NOTIFICATION_CHANNEL_ID,
                Constants.NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW).apply {
                description = "Upload in progress"
            }
            notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val cancelText = "Stop Upload"
        val cancelPendingIntent = WorkManager.getInstance(context).createCancelPendingIntent(id)

        return NotificationCompat.Builder(
            context, Constants.NOTIFICATION_CHANNEL_ID
        )
            .setContentTitle("Uploading Progress")
            .setContentText("Kindly wait upload is on-going")
            //.setTicker(title)
            .setProgress(100, downloadProgress, false)
            .setSmallIcon(R.drawable.ic_round_notifications_24)
            .setOngoing(true)
            .addAction(R.drawable.ic_baseline_close_black_24, cancelText, cancelPendingIntent)
            .build()
    }

    private fun setFileInputOutStream(selectedImageUri: Uri): File {
        val parcelFileDescriptor = context.contentResolver.openFileDescriptor(
            selectedImageUri, "r", null)

        val inputStream = FileInputStream(parcelFileDescriptor!!.fileDescriptor)
        val file = File(context.cacheDir, context.getFileName(selectedImageUri)!!)
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        return file
    }

    private fun requestBodyQueries(): HashMap<String, RequestBody> {
        val partQueriesBody: HashMap<String, RequestBody> = HashMap()
        partQueriesBody["apiKey"]       = XtremeCardzUtils.createPartFromString(Constants.API_KEY)
        partQueriesBody["project_code"] = XtremeCardzUtils.createPartFromString(inputData.getString("project")!!)
        partQueriesBody["uid"]          = XtremeCardzUtils.createPartFromString(inputData.getString("uid")!!)
        return partQueriesBody
    }

}