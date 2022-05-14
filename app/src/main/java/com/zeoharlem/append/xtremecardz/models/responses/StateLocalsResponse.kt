package com.zeoharlem.append.xtremecardz.models.responses


import com.google.gson.annotations.SerializedName
import com.zeoharlem.append.xtremecardz.models.StateLocals

data class StateLocalsResponse(
    @SerializedName("results")
    val results: List<StateLocals>,
    @SerializedName("status")
    val status: String
)