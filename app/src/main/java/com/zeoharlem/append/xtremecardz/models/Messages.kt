package com.zeoharlem.append.xtremecardz.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.sql.ClientInfoStatus

@Parcelize
data class Messages(
    @SerializedName("date_created")
    val dateCreated: String,
    @SerializedName("exchange_item_img")
    val exchangeItemImg: String,
    @SerializedName("provider_item_img")
    val providerItemImg: String,
    @SerializedName("provider_name")
    val providerName: String,
    @SerializedName("provider_user_id")
    val providerUserId: String,
    @SerializedName("receiver_image")
    val receiverImage: String,
    @SerializedName("receiver_name")
    val receiverName: String,
    @SerializedName("provider_peering_id")
    val providerPeeringId: String,
    @SerializedName("provider_item_add")
    val providerItemAdd: String,
    @SerializedName("provider_item_desc")
    val providerItemDesc: String,
    @SerializedName("receiver_uid")
    val receiverUid: String? = null,
    @SerializedName("provider_image")
    val providerImage: String? = null,
    @SerializedName("provider_title")
    val providerTitle: String? = null,
    @SerializedName("exchange_title")
    val exchangeTitle: String? = null,
    @SerializedName("receiver_phone")
    val receiverPhone: String? = null,
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("barter_product_id")
    val barterProductId: String? = null,
    @SerializedName("barter_exchange_id")
    val barterExchangeId: String? = null,
): Parcelable