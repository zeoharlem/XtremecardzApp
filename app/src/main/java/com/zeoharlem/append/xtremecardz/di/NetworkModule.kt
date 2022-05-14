package com.zeoharlem.append.xtremecardz.di

import com.zeoharlem.append.xtremecardz.services.XtremecardzServices
import com.zeoharlem.append.xtremecardz.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient{
        return OkHttpClient.Builder()
            .callTimeout(2, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory{
        return GsonConverterFactory.create()
    }

    @Singleton
    @Provides
    fun provideRetrofitInstance(okHttpClient: OkHttpClient, gsonConverterFactory: GsonConverterFactory): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): XtremecardzServices {
        return retrofit.create(XtremecardzServices::class.java)
    }
}