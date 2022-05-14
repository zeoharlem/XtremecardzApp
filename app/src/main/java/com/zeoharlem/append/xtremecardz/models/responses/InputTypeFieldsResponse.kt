package com.zeoharlem.append.xtremecardz.models.responses


import com.google.gson.annotations.SerializedName
import com.zeoharlem.append.xtremecardz.models.InputTypeFields

data class InputTypeFieldsResponse(
    @SerializedName("results")
    val results: List<InputTypeFields>,
    @SerializedName("status")
    val status: String
)