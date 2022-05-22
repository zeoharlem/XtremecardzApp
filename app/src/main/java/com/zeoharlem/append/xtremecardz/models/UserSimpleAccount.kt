package com.zeoharlem.append.xtremecardz.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserSimpleAccount(
    @SerializedName("firstname")
    val firstName: String? = null,
    @SerializedName("lastname")
    val lastName: String? = null,
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("password")
    val password: String? = null,
    @SerializedName("phonenumber")
    val phoneNumber: String? = null,
    @SerializedName("uid")
    val uid: String? = null,
    @SerializedName("token")
    val token: String? = null,
    @SerializedName("_id")
    val id: String? = null
): Parcelable
