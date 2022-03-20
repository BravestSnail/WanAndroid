package com.breavestsnail.wanandroid.logic

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.breavestsnail.wanandroid.logic.model.Article


class ArticleDataSource(val cid:Int?=null): PagingSource<Int, Article>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        try {
            val curPageNumber = params.key?:0
            val response = Repository.getActicle(curPageNumber,cid)
            val nextPageNumber = if (response.data.curPage<=response.data.pageCount) response.data.curPage else null
            Log.d("test", "load: $cid,$curPageNumber,$nextPageNumber")
            return LoadResult.Page(
                data = response.data.articles,
                prevKey = null,
                nextKey = nextPageNumber
            )
        }catch (e:Exception){
            return LoadResult.Error<Int, Article>(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1)?:anchorPage?.nextKey?.minus(1)
        }
    }

}