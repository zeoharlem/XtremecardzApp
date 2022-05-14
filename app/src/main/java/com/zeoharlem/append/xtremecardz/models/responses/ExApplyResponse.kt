package com.zeoharlem.append.xtremecardz.models.responses

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExApplyResponse (
    @SerializedName("status")
    val status: String,
    @SerializedName("message")
    val message: String
): Parcelable