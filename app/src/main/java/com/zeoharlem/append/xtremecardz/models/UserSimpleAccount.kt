package com.zeoharlem.append.xtremecardz.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserSimpleAccount(
    @SerializedName("firstname")
    val firstName: String,
    @SerializedName("lastname")
    val lastName: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("phonenumber")
    val phoneNumber: String,
    @SerializedName("uid")
    val uid: String
): Parcelable
