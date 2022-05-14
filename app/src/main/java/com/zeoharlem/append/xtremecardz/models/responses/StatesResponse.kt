package com.zeoharlem.append.xtremecardz.models.responses


import com.google.gson.annotations.SerializedName
import com.zeoharlem.append.xtremecardz.models.States

data class StatesResponse(
    @SerializedName("results")
    val results: List<States>,
    @SerializedName("status")
    val status: String
)