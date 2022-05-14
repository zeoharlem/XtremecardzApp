package com.zeoharlem.append.xtremecardz.models


import com.google.gson.annotations.SerializedName

data class Subcategory(
    @SerializedName("category_id")
    val categoryId: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("slug")
    val slug: String,
    @SerializedName("subcategory_id")
    val subcategoryId: String,
    @SerializedName("title")
    val title: String
)