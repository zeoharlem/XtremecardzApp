package com.zeoharlem.append.xtremecardz.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Local(
    @SerializedName("location_id")
    val locationId: String,
    @SerializedName("places_id")
    val placesId: String,
    @SerializedName("title")
    val title: String
): Parcelable