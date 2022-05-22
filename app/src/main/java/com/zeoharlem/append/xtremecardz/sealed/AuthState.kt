package com.zeoharlem.append.xtremecardz.sealed

sealed class AuthState<T>(val dataSource: T? = null, val message: String? = null) {
    class Idle<T>: AuthState<T>()
    class Success<T>(dataSource: T?): AuthState<T>(dataSource)
    class Error<T>(message: String?, dataSource: T? = null): AuthState<T>(dataSource, message)
    class Loading<T>: AuthState<T>()
}
