package com.zeoharlem.append.xtremecardz.di.repository

import com.zeoharlem.append.xtremecardz.datasources.RemoteDataSource
import com.zeoharlem.append.xtremecardz.datasources.ZipRepository
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class Repository @Inject constructor(
    val remoteDataSource: RemoteDataSource,
    val zipRepository: ZipRepository) {
}