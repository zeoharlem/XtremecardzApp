package com.zeoharlem.append.xtremecardz.models

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SelectedImages(var capturedItem: Bitmap?, var filePath: String = "") : Parcelable
