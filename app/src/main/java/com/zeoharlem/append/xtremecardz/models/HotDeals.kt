package com.zeoharlem.append.xtremecardz.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
//@Entity
data class HotDeals(
    @SerializedName("category")
    val category: String,
//    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    val id: String,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("location")
    val location: String,
    @SerializedName("place")
    val place: String,
    @SerializedName("subcategory")
    val subcategory: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("fullname")
    val fullName: String,
    @SerializedName("profile_image")
    val profileImage: String,
    @SerializedName("date_created")
    val dateCreated: String,
    @SerializedName("contact_phone")
    val contactPhone: String,
    @SerializedName("raw_data")
    val rawData: String,
    @SerializedName("specific_item_title")
    val specificItemTitle: String,
    @SerializedName("exchange_options")
    val exchangeOptions: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("peering_status")
    val peeringStatus: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("email")
    val email: String? = null,
): Parcelable