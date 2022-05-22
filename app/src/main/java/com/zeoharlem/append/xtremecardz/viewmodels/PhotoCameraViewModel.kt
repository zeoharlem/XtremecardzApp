package com.zeoharlem.append.xtremecardz.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeoharlem.append.xtremecardz.di.repository.Repository
import com.zeoharlem.append.xtremecardz.sealed.NetworkResults
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel
class PhotoCameraViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    private var _submitResponseBody = MutableLiveData<NetworkResults<ResponseBody>>()
    val submitResponseBody get() = _submitResponseBody

    fun submitProjectInfoAction(token: String, queries: HashMap<String, RequestBody>, file: MultipartBody.Part)   = viewModelScope.launch {
        submitProjectInfoActionCall(token, queries, file)
    }

    private suspend fun submitProjectInfoActionCall(token: String, queries: HashMap<String, RequestBody>, file: MultipartBody.Part){
        _submitResponseBody.value   = NetworkResults.Loading()
        try{
            val response    = repository.remoteDataSource.submitProjectInfoRepository(token, queries, file)
            _submitResponseBody.value   = NetworkResults.Success(response)
            Log.e("PhotoCameraViewModel", "submitProjectInfoActionCall: ${response.string()}", )
        }
        catch (e: Exception){
            _submitResponseBody.value   = NetworkResults.Error(e.localizedMessage)
            Log.e("PhotoCameraViewModel", "error call: ${e.localizedMessage}", )
        }
    }
}