package com.zeoharlem.append.xtremecardz.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BarterProductDetails(
    @SerializedName("barter_product_id")
    val barterProductId: String,
    @SerializedName("barter_status")
    val barterStatus: String,
    @SerializedName("category_id")
    val categoryId: String,
    @SerializedName("contact_phone")
    val contactPhone: String,
    @SerializedName("creator_id")
    val creatorId: String,
    @SerializedName("date_created")
    val dateCreated: String,
    @SerializedName("exchange_options")
    val exchangeOptions: String,
    @SerializedName("front_image")
    val frontImage: String,
    @SerializedName("geo_address")
    val geoAddress: String,
    @SerializedName("ip_address")
    val ipAddress: String,
    @SerializedName("latitude")
    val latitude: String,
    @SerializedName("location")
    val location: String,
    @SerializedName("location_id")
    val locationId: String,
    @SerializedName("longitude")
    val longitude: String,
    @SerializedName("package_type")
    val packageType: String,
    @SerializedName("peering_status")
    val peeringStatus: String,
    @SerializedName("place")
    val place: String,
    @SerializedName("place_id")
    val placeId: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("product_description")
    val productDescription: String,
    @SerializedName("product_title")
    val productTitle: String,
    @SerializedName("publish")
    val publish: String,
    @SerializedName("raw_data")
    val rawData: String,
    @SerializedName("slug")
    val slug: String,
    @SerializedName("specific_item_title")
    val specificItemTitle: String,
    @SerializedName("subcategory_id")
    val subcategoryId: String
): Parcelable