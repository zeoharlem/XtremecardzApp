package com.zeoharlem.append.xtremecardz.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Profile(
    @SerializedName("phone")
    val phone: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("fullname")
    val fullname: String,
    @SerializedName("date_created")
    val dateCreated: String? = null,
    @SerializedName("uid")
    val uid: String,
    @SerializedName("company_name")
    val companyName: String,
    @SerializedName("designation")
    val designation: String,
    @SerializedName("company_address")
    val companyAddress: String,
    @SerializedName("back_content_desc")
    val backContentDesc: String,
    @SerializedName("profile_image")
    val profileImage: String? = null,
    @SerializedName("website")
    val websiteLink: String? = null,
    @SerializedName("card_type")
    val cardType: String? = null,
    @SerializedName("card_number")
    val cardNumbers: String? = null,
    @SerializedName("project_code")
    val project_code: String? = null,
): Parcelable

