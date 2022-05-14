package com.zeoharlem.append.xtremecardz.models


import com.google.gson.annotations.SerializedName

data class StateLocals(
    @SerializedName("location_id")
    val locationId: String,
    @SerializedName("places_id")
    val placesId: String,
    @SerializedName("title")
    val title: String
)