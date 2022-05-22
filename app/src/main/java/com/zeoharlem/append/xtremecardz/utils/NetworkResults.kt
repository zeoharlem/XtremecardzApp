package com.zeoharlem.append.xtremecardz.utils

sealed class NetworkResults<T>(val dataSource: T? = null, val message: String? = null) {

    class Success<T>(dataSource: T?): NetworkResults<T>(dataSource)
    class Error<T>(message: String?, dataSource: T? = null): NetworkResults<T>(dataSource, message)
    class Loading<T>: NetworkResults<T>()
}
