package com.zeoharlem.append.xtremecardz.models.responses


import com.google.gson.annotations.SerializedName
import com.zeoharlem.append.xtremecardz.models.Categories

data class CategoriesResponse(
    @SerializedName("results")
    val results: List<Categories>,
    @SerializedName("status")
    val status: String
)