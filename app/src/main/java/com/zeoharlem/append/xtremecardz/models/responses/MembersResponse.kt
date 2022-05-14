package com.zeoharlem.append.xtremecardz.models.responses


import com.google.gson.annotations.SerializedName
import com.zeoharlem.append.xtremecardz.models.Members

data class MembersResponse(
    @SerializedName("results")
    val results: List<Members>,
    @SerializedName("status")
    val status: String
)