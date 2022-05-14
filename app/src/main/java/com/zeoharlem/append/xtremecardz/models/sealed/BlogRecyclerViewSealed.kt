package com.zeoharlem.append.xtremecardz.models.sealed

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

sealed class BlogRecyclerViewSealed {

    @Parcelize
    data class Blog(
        @SerializedName("blog_id")
        val blogId: Int,
        @SerializedName("blog_title")
        val blogTitle: String,
        @SerializedName("blog_desc")
        val blogDesc: String,
        @SerializedName("blog_image")
        val blogImage: String,
        @SerializedName("blog_date")
        val blogDate: String,
    ): BlogRecyclerViewSealed(), Parcelable

    @Parcelize
    data class Tips(
        @SerializedName("blog_id")
        val tipsId: Int,
        @SerializedName("blog_title")
        val tipsTitle: String,
        @SerializedName("blog_desc")
        val tipsDesc: String,
        @SerializedName("blog_image")
        val tipsImage: String,
        @SerializedName("blog_date")
        val tipsDate: String,
    ): BlogRecyclerViewSealed(), Parcelable

    data class HeaderTitle(
        val id: Int,
        val title: String,
    ): BlogRecyclerViewSealed()
}