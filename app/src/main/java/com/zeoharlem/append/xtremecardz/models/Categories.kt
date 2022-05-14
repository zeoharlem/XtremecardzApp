package com.zeoharlem.append.xtremecardz.models


import com.google.gson.annotations.SerializedName

data class Categories(
    @SerializedName("category_id")
    val categoryId: String,
    @SerializedName("category_image")
    val categoryImage: String,
    @SerializedName("created")
    val created: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("slug")
    val slug: String
)