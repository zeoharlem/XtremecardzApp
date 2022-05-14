package com.zeoharlem.append.xtremecardz.models.responses


import com.google.gson.annotations.SerializedName
import com.zeoharlem.append.xtremecardz.models.BarterProducts

data class PublishResponse(
    @SerializedName("results")
    val results: BarterProducts,
    @SerializedName("status")
    val status: String
)