package com.zeoharlem.append.xtremecardz.models.responses


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import com.zeoharlem.append.xtremecardz.models.Posts

@Parcelize
data class PaymentResponse(
    @SerializedName("authorize_url")
    val authorizeUrl: String,
    @SerializedName("posts")
    val posts: Posts,
    @SerializedName("reference")
    val reference: String,
    @SerializedName("status")
    val status: String
): Parcelable