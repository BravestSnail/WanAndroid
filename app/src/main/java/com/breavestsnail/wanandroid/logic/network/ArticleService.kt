package com.breavestsnail.wanandroid.logic.network

import com.breavestsnail.wanandroid.logic.model.ArticleData
import com.breavestsnail.wanandroid.logic.model.Chapter
import com.breavestsnail.wanandroid.logic.model.WanAndroidResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ArticleService {
    /*
    * 按页获取站内文章*/
    @GET("article/list/{page}/json")
    fun getArticle(@Path("page") page:Int,@Query("cid")cid:Int?): Call<WanAndroidResponse<ArticleData>>
    /*
    * 获取置顶文章*/
    @GET("article/top/json")
    fun getTopArticle(): Call<WanAndroidResponse<ArticleData>>

    /*
    * 搜索站内文章*/
    @POST("/article/query/{page}/json")
    fun getSearchList(@Path("page")page: Int,@Query("k")key:String): Call<WanAndroidResponse<ArticleData>>

    /*
    * 获取体系结构数据*/
    @GET("/tree/json")
    fun getChapter(): Call<WanAndroidResponse<List<Chapter>>>

}