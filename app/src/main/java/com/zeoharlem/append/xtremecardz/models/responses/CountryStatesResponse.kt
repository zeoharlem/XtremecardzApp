package com.zeoharlem.append.xtremecardz.models.responses


import com.google.gson.annotations.SerializedName
import com.zeoharlem.append.xtremecardz.models.CountryStates

data class CountryStatesResponse(
    @SerializedName("results")
    val results: List<CountryStates>,
    @SerializedName("status")
    val status: String
)