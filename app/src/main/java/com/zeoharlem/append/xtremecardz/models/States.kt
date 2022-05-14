package com.zeoharlem.append.xtremecardz.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class States(
    @SerializedName("description")
    val description: String,
    @SerializedName("locals")
    val locals: ArrayList<Local>,
    @SerializedName("location_id")
    val locationId: String,
    @SerializedName("slug")
    val slug: String,
    @SerializedName("title")
    val title: String
): Parcelable