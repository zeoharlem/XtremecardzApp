package com.zeoharlem.append.xtremecardz.models


import com.google.gson.annotations.SerializedName

data class SilentRegister(
    @SerializedName("results")
    val results: Boolean,
    @SerializedName("status")
    val status: String
)