package com.zeoharlem.append.xtremecardz.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Transaction(
    @SerializedName("address")
    val address: String,
    @SerializedName("client_uid")
    val clientUid: String,
    @SerializedName("created")
    val created: String,
    @SerializedName("distance")
    val distance: String,
    @SerializedName("driver_uid")
    val driverUid: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("estimated")
    val estimated: String,
    @SerializedName("firstname")
    val firstname: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("lastname")
    val lastname: String,
    @SerializedName("latitude")
    val latitude: String,
    @SerializedName("longitude")
    val longitude: String,
    @SerializedName("reference")
    val reference: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("trans_id")
    val transId: String
): Parcelable