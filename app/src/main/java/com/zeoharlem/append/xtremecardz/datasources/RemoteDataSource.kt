package com.zeoharlem.append.xtremecardz.datasources

import com.zeoharlem.append.xtremecardz.models.UserSimpleAccount
import com.zeoharlem.append.xtremecardz.services.XtremecardzServices
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val xtremecardzServices: XtremecardzServices){

    suspend fun submitProjectInfoRepository(token: String, queries: HashMap<String, RequestBody>, file: MultipartBody.Part): ResponseBody {
        return xtremecardzServices.submitProjectInfo(token, queries, file)
    }

    suspend fun registerUserSilently(queries: HashMap<String, String>): Response<UserSimpleAccount> {
        return xtremecardzServices.registerUserSilently(queries)
    }

    suspend fun login(queries: HashMap<String, String>): Response<UserSimpleAccount> {
        return xtremecardzServices.login(queries)
    }
}