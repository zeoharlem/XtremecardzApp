package com.zeoharlem.append.xtremecardz.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class InputTypeFields(
    @SerializedName("attribute_id")
    val attributeId: String,
    @SerializedName("barter_product_attribute_id")
    val barterProductAttributeId: String,
    @SerializedName("character_counter")
    val characterCounter: String,
    @SerializedName("hint")
    val hint: String,
    @SerializedName("input_type")
    val inputType: String,
    @SerializedName("label")
    val label: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("placeholder")
    val placeholder: String,
    @SerializedName("property_group")
    val propertyGroup: String,
    @SerializedName("title")
    val title: String,
//    @SerializedName("validation")
//    val validation: Any,
    @SerializedName("values")
    val values: ArrayList<Value>
): Parcelable