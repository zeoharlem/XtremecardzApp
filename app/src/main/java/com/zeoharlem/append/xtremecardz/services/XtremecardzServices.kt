package com.zeoharlem.append.xtremecardz.services

import com.zeoharlem.append.xtremecardz.models.UserSimpleAccount
import com.zeoharlem.append.xtremecardz.models.responses.ProjectInfoResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface XtremecardzServices {

    @Multipart
    @POST("project/create")
    suspend fun submitProjectInfo(
        @Header("Authorization") token: String,
        @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part files: MultipartBody.Part): ResponseBody

    @Multipart
    @POST("project/upload")
    suspend fun uploadFile(
        @Header("Authorization") token: String,
        @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part files: MultipartBody.Part): ResponseBody

    @GET("project/{page}/{limit}")
    suspend fun getListsProjects(
        @Header("Authorization") token: String,
        @Path("page") page: Int,
        @Path("limit") limit: Int): Response<ProjectInfoResponse>

    @FormUrlEncoded
    @POST("users/")
    suspend fun registerUserSilently(@FieldMap partMap: Map<String, String>): Response<UserSimpleAccount>

    @FormUrlEncoded
    @POST("users/login")
    suspend fun login(@FieldMap partMap: Map<String, String>): Response<UserSimpleAccount>
}