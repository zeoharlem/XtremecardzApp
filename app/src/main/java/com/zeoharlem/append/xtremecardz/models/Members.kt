package com.zeoharlem.append.xtremecardz.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Members(
    @SerializedName("date_created")
    val dateCreated: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("fullname")
    val fullname: String,
    @SerializedName("profile_image")
    val profileImage: String,
    @SerializedName("uid")
    val uid: String,
    @SerializedName("phone")
    val phone: String? = null
): Parcelable