package com.zeoharlem.append.xtremecardz.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Category(
    @SerializedName("category_id")
    val categoryId: Int,
    @SerializedName("category_image")
    val categoryImage: String,
    @SerializedName("created")
    val created: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("slug")
    val slug: String,
    @SerializedName("subcategory")
    val subcategory: ArrayList<SubcategoryX>
): Parcelable