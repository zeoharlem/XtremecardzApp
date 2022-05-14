package com.zeoharlem.append.xtremecardz.models.responses


import com.google.gson.annotations.SerializedName
import com.zeoharlem.append.xtremecardz.models.Category

data class CategoryResponse(
    @SerializedName("results")
    val results: List<Category>,
    @SerializedName("status")
    val status: String
)