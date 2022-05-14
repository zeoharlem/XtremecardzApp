package com.zeoharlem.append.xtremecardz.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BarterProducts(
    @SerializedName("apiKey")
    val apiKey: String,
    @SerializedName("barter_product_id")
    val barterProductId: String? = null
): Parcelable