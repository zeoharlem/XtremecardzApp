package com.zeoharlem.append.xtremecardz.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.zeoharlem.append.xtremecardz.datasources.ProjectInfoPagingSource
import com.zeoharlem.append.xtremecardz.di.repository.Repository
import com.zeoharlem.append.xtremecardz.models.ProjectInfoX
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ProjectsViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    fun getProjectXPagingItem(token: String): Flow<PagingData<ProjectInfoX>> = Pager(
        config = PagingConfig(pageSize = 20, prefetchDistance = 2)
    ) {
        ProjectInfoPagingSource(token, repository)
    }.flow.cachedIn(viewModelScope)
}