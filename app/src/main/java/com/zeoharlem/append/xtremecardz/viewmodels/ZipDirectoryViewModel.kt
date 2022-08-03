package com.zeoharlem.append.xtremecardz.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeoharlem.append.xtremecardz.datasources.ZipRepository
import com.zeoharlem.append.xtremecardz.di.repository.Repository
import com.zeoharlem.append.xtremecardz.models.CapturedImage
import com.zeoharlem.append.xtremecardz.sealed.NetworkResults
import com.zeoharlem.append.xtremecardz.sealed.ZipDirectoryState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ZipDirectoryViewModel @Inject constructor(
    application:Application,
    val repository: Repository
    ): BaseAppViewModel(application) {

    //Watch the upload/percentage progress
    private var _uploadFileProgress = MutableLiveData<NetworkResults<ResponseBody>>()
    val uploadFileProgress get() = _uploadFileProgress

    private var _taskCompletedAction = MutableLiveData(false)
    val taskCompletedAction get() = _taskCompletedAction

    //Watch the zip/compression progress
    private var _zipFileProgress = MutableLiveData<ZipDirectoryState<String>>()
    val zipFileProgress get() = _zipFileProgress

    //Create the zip directory and zip the created directory
    fun createZipDirectory(folder: String, fileCollection: ArrayList<CapturedImage>) = viewModelScope.launch {
        zipDirectory(folder, fileCollection)
    }

    //Upload the selected file request
    fun uploadFileRequest(token: String, query: HashMap<String, RequestBody>, file: MultipartBody.Part) = viewModelScope.launch {
        uploadRequestBodyEvent(token, query, file)
    }

    //Set/Check completion of task
    fun setTaskCompletionAction(boolean: Boolean){
        _taskCompletedAction.value = boolean
    }

    private suspend fun uploadRequestBodyEvent(token: String, query: HashMap<String, RequestBody>, file: MultipartBody.Part){
        _uploadFileProgress.value = NetworkResults.Loading()
        try {
            val response = repository.zipRepository.uploadFile(token, query, file)
            Log.e("ZipDirectory", "uploadRequestBodyEvent: ${response.string()}", )
            _uploadFileProgress.value = NetworkResults.Success(response)
        }
        catch (e: Exception){
            _uploadFileProgress.value = NetworkResults.Error(e.localizedMessage)
            Log.e("ZipDirectoryError", "uploadRequestBodyEvent: ${e.localizedMessage}", )
        }
    }

    private fun zipDirectory(folder: String, fileCollection: ArrayList<CapturedImage>){
        _zipFileProgress.value = ZipDirectoryState.Loading()
        try{
            val filePath = repository.zipRepository.createZipDirectory(context, folder, fileCollection)
            _zipFileProgress.value = ZipDirectoryState.Success(filePath)
        }
        catch (e: Exception){
            _zipFileProgress.value = ZipDirectoryState.Error(e.localizedMessage)
        }
    }

}