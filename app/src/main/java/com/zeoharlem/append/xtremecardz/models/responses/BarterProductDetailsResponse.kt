package com.zeoharlem.append.xtremecardz.models.responses


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import com.zeoharlem.append.xtremecardz.models.BarterProductDetails
import com.zeoharlem.append.xtremecardz.models.Image

@Parcelize
data class BarterProductDetailsResponse(
    @SerializedName("images")
    val images: List<Image>,
    @SerializedName("results")
    val results: BarterProductDetails,
    @SerializedName("status")
    val status: String
): Parcelable