package com.zeoharlem.append.xtremecardz.models

import com.google.gson.annotations.SerializedName

data class Users(
    @SerializedName("uid")
    val uid: String,
    @SerializedName("incoming")
    val incoming: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("jroom")
    val jroom: String
)
