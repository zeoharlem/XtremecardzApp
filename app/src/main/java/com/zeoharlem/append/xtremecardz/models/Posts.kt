package com.zeoharlem.append.xtremecardz.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Posts(
    @SerializedName("amount")
    val amount: String,
    @SerializedName("apiKey")
    val apiKey: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("firstname")
    val firstname: String,
    @SerializedName("lastname")
    val lastname: String,
    @SerializedName("subscription")
    val subscription: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("reference")
    val reference: String? = null
): Parcelable