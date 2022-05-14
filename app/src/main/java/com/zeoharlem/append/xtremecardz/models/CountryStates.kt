package com.zeoharlem.append.xtremecardz.models


import com.google.gson.annotations.SerializedName

data class CountryStates(
    @SerializedName("description")
    val description: String,
    @SerializedName("location_id")
    val locationId: String,
    @SerializedName("slug")
    val slug: String,
    @SerializedName("title")
    val title: String
)