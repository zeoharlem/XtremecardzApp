package com.zeoharlem.append.xtremecardz.services

import com.zeoharlem.append.xtremecardz.utils.Constants
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface MyApi {

    @Multipart
    @POST("project/create")
    fun submitProjectInfo(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part,
        @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>,
    ): Call<ResponseBody>

    @Multipart
    @POST("project/upload")
    fun uploadImage(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part,
        @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>,
    ): Call<ResponseBody>

    @Multipart
    @POST("project/uploadmulti")
    fun uploadMultipleImage(
        @Header("Authorization") token: String,
        @Part multipleImages: List<MultipartBody.Part>,
        @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>,
    ): Call<ResponseBody>

    companion object {
        operator fun invoke(): MyApi {
            val client = OkHttpClient.Builder()
                .callTimeout(2, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                //.retryOnConnectionFailure(true)
                .build()

            return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(MyApi::class.java)
        }
    }
}