package com.zeoharlem.append.xtremecardz.models.responses


import com.google.gson.annotations.SerializedName
import com.zeoharlem.append.xtremecardz.models.ProjectInfoX

data class ProjectInfoResponse(
    @SerializedName("currentPage")
    val currentPage: String?,
    @SerializedName("projectInfo")
    val projectInfo: List<ProjectInfoX>?,
    @SerializedName("totalPages")
    val totalPages: Int?
)