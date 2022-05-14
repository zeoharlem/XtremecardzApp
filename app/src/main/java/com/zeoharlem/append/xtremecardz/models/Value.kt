package com.zeoharlem.append.xtremecardz.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Value(
    @SerializedName("attribute_id")
    val attributeId: String,
    @SerializedName("barter_product_attribute_id")
    val barterProductAttributeId: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("value_name")
    val valueName: String
): Parcelable