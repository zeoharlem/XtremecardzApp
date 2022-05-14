package com.zeoharlem.append.xtremecardz.models.responses

import com.google.gson.annotations.SerializedName
import com.zeoharlem.append.xtremecardz.models.sealed.BlogRecyclerViewSealed

class BlogResponse(
    @SerializedName("results")
    val results: List<BlogRecyclerViewSealed.Blog>,
    @SerializedName("status")
    val status: String
)