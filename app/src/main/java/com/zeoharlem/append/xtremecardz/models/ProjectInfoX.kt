package com.zeoharlem.append.xtremecardz.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProjectInfoX(
    @SerializedName("back_content")
    val backContent: String?,
    @SerializedName("company_address")
    val companyAddress: String?,
    @SerializedName("company_name")
    val companyName: String?,
    @SerializedName("createdAt")
    val createdAt: String?,
    @SerializedName("designation")
    val designation: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("fullname")
    val fullname: String?,
    @SerializedName("_id")
    val id: String?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("number_of_cards")
    val numberOfCards: Int?,
    @SerializedName("phone_number")
    val phoneNumber: String?,
    @SerializedName("uid")
    val uid: String?,
    @SerializedName("updatedAt")
    val updatedAt: String?,
    @SerializedName("users")
    val users: String?,
    @SerializedName("website_link")
    val websiteLink: String?,
    @SerializedName("card_type")
    val cardType: String? = null,
): Parcelable