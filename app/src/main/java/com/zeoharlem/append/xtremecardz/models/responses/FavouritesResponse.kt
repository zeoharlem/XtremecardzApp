package com.zeoharlem.append.xtremecardz.models.responses

import android.view.SearchEvent
import com.zeoharlem.append.xtremecardz.models.Favourites

data class FavouritesResponse(
    val response: String,
    val totalResult: String,
    val search: List<Favourites>
)
