package com.breavestsnail.wanandroid.logic.network

import com.breavestsnail.wanandroid.logic.model.User
import com.breavestsnail.wanandroid.logic.model.WanAndroidResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserService{
    /**
     * 登录
     */
    @POST("/user/login")
    suspend fun login(
        @Query("username") username: String,
        @Query("password") password: String
    ): WanAndroidResponse<User>

    /**
     * 注册成功和登录成功的json一样，通过判断errorCode即可。
     */
    @POST("/user/register")
    suspend fun register(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("repassword") repassword: String
    ): WanAndroidResponse<User>

    /**
     * 退出,todo 清理cookie
     * 退出成功为是判断errorCode即可：{"data":null,"errorCode":0,"errorMsg":""}
     */
    @GET("/user/logout/json")
    suspend fun logout(): WanAndroidResponse<User>
}