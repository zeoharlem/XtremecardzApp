package com.zeoharlem.append.xtremecardz.models.responses

import com.google.gson.annotations.SerializedName
import com.zeoharlem.append.xtremecardz.models.sealed.BlogRecyclerViewSealed

class TipsResponse(
    @SerializedName("results")
    val results: List<BlogRecyclerViewSealed.Tips>,
    @SerializedName("status")
    val status: String
)