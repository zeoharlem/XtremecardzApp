package com.zeoharlem.append.xtremecardz.models.responses


import com.google.gson.annotations.SerializedName
import com.zeoharlem.append.xtremecardz.models.DetailsImages

data class DetailsImagesResponse(
    @SerializedName("results")
    val results: List<DetailsImages>,
    @SerializedName("status")
    val status: String
)