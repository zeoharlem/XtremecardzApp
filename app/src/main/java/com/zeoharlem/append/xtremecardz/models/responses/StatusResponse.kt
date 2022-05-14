package com.zeoharlem.append.xtremecardz.models.responses

import com.google.gson.annotations.SerializedName

data class StatusResponse(
    @SerializedName("status")
    val status: Boolean
)
