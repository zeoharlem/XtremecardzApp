package com.zeoharlem.append.xtremecardz.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SubmitFormFields(
    val key: String? = null,
    val value: String? = null
): Parcelable
