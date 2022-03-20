package com.breavestsnail.wanandroid.logic.network

import com.breavestsnail.wanandroid.logic.model.ArticleData
import com.breavestsnail.wanandroid.logic.model.Chapter
import com.breavestsnail.wanandroid.logic.model.WanAndroidResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProjectService {
    @GET("/project/tree/json")
    fun getProjectChapter(): Call<WanAndroidResponse<List<Chapter>>>

    @GET("/project/list/{page}/json")
    fun getProjectArticle(@Path("page")page:Int,@Query("cid")cid:Int?):Call<WanAndroidResponse<ArticleData>>
}