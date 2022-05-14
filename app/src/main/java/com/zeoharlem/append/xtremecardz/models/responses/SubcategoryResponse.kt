package com.zeoharlem.append.xtremecardz.models.responses


import com.google.gson.annotations.SerializedName
import com.zeoharlem.append.xtremecardz.models.Subcategory

data class SubcategoryResponse(
    @SerializedName("results")
    val results: List<Subcategory>,
    @SerializedName("status")
    val status: String
)