package com.zeoharlem.append.xtremecardz.datasources

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zeoharlem.append.xtremecardz.di.repository.Repository
import com.zeoharlem.append.xtremecardz.models.ProjectInfoX
import retrofit2.HttpException
import java.io.IOException

class ProjectInfoPagingSource(private val token: String, private val repository: Repository): PagingSource<Int, ProjectInfoX>() {
    override fun getRefreshKey(state: PagingState<Int, ProjectInfoX>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1) ?:
            state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProjectInfoX> {
        return try{
            val page        = params.key ?: 1
            val response    = repository.remoteDataSource.getProjectInfoPagingAction(token, page, 20)
            //val dataRepo    = response.body()?.results ?: emptyList()
            //Log.e("HottestCollection", "load: ${response.raw()}", )
            LoadResult.Page(
                data    = response.body()?.projectInfo ?: emptyList(),
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.body()!!.projectInfo!!.isEmpty()) null else page.plus(1)
            )
        }
        catch (e: IOException){
            LoadResult.Error(e)
        }
        catch (e: HttpException){
            LoadResult.Error(e)
        }
    }
}