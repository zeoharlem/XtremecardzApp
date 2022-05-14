package com.zeoharlem.append.xtremecardz.models.responses


import com.google.gson.annotations.SerializedName
import com.zeoharlem.append.xtremecardz.models.Messages

data class MessagesResponses(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<Messages>,
    @SerializedName("status")
    val status: String,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)