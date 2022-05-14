package com.zeoharlem.append.xtremecardz.models.responses

import com.google.gson.annotations.SerializedName
import com.zeoharlem.append.xtremecardz.models.sealed.BlogRecyclerViewSealed

class ResultsResponse<T>(
    @SerializedName("results")
    val results: List<T>,
    @SerializedName("status")
    val status: String
)