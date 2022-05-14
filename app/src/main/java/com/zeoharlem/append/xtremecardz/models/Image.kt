package com.zeoharlem.append.xtremecardz.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Image(
    @SerializedName("barter_product_id")
    val barterProductId: String,
    @SerializedName("barter_product_image_id")
    val barterProductImageId: String,
    @SerializedName("display_status")
    val displayStatus: String,
    @SerializedName("image_url")
    val imageUrl: String
): Parcelable