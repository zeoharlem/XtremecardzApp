package com.zeoharlem.append.xtremecardz.sealed

import java.io.File

sealed class ZipDirectoryState<T>(val dataSource: T? = null, val message: String? = null){
    class Success<T>(dataSource: T?): ZipDirectoryState<T>(dataSource)
    class Error<T>(message: String?, dataSource: T? = null): ZipDirectoryState<T>(dataSource, message)
    class Loading<T>: ZipDirectoryState<T>()
}
