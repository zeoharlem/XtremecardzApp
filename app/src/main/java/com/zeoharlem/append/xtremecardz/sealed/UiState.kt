package com.zeoharlem.append.xtremecardz.sealed

sealed class UiState{
    object Loading : UiState()
    data class Success(val results: List<*>): UiState()
    data class Error(val message: String): UiState()
}
