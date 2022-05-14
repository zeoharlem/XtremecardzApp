package com.zeoharlem.append.xtremecardz.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LatestCollection(
    @SerializedName("category")
    val category: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("location")
    val location: String,
    @SerializedName("place")
    val place: String,
    @SerializedName("subcategory")
    val subcategory: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("fullname")
    val fullName: String,
    @SerializedName("profile_image")
    val profileImage: String
): Parcelable