package com.breavestsnail.wanandroid.logic.network

import com.breavestsnail.wanandroid.logic.model.Banner
import com.breavestsnail.wanandroid.logic.model.WanAndroidResponse
import retrofit2.Call
import retrofit2.http.GET

interface BannerService {
    @GET("/banner/json")
    fun getBanner():Call<WanAndroidResponse<List<Banner>>>
}