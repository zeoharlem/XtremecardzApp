package com.zeoharlem.append.xtremecardz.models

import com.google.gson.annotations.SerializedName

data class FcmData(
    @SerializedName("token")
    val token: String,
    @SerializedName("uid")
    val uid: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("jroom")
    val jroom: String,
    @SerializedName("incoming")
    val incoming: String
)
