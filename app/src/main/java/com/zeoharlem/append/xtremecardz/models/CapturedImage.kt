package com.zeoharlem.append.xtremecardz.models

import android.graphics.Bitmap
import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CapturedImage(var capturedItem: Uri?, var filePath: String = "") : Parcelable