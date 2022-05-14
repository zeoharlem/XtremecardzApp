package com.zeoharlem.append.xtremecardz.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SubcategoryX(
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
): Parcelable