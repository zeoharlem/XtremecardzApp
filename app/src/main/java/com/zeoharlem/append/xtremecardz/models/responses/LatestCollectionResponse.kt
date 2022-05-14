package com.zeoharlem.append.xtremecardz.models.responses


import com.google.gson.annotations.SerializedName
import com.zeoharlem.append.xtremecardz.models.LatestCollection

data class LatestCollectionResponse(
    @SerializedName("results")
    val results: List<LatestCollection>,
    @SerializedName("status")
    val status: String
)