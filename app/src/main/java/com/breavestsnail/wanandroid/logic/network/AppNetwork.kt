package com.breavestsnail.wanandroid.logic.network

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object AppNetwork {
    private val bannerService = ServiceCreator.create(BannerService::class.java)

    suspend fun getBanner() = bannerService.getBanner().await()

    private val articleService = ServiceCreator.create(ArticleService::class.java)

    suspend fun getArticle(page:Int,cid:Int?) = articleService.getArticle(page,cid).await()

    suspend fun getTopActicle() = articleService.getTopArticle().await()

    suspend fun getSearchList(page: Int,key:String) = articleService.getSearchList(page, key).await()

    suspend fun getChapter() = articleService.getChapter().await()

    private val projectService = ServiceCreator.create<ProjectService>()

    suspend fun getProjectChapter() = projectService.getProjectChapter().await()

    suspend fun getProjectActicle(page: Int,cid: Int?) = projectService.getProjectArticle(page, cid).await()

    private val userService = ServiceCreator.create<UserService>()

    suspend fun login(username:String,password:String) = userService.login(username, password)

    suspend fun register(username: String,password: String,repassword:String) = userService.register(username, password, repassword)

    suspend fun logout() = userService.logout()


     suspend fun <T> Call<T>.await():T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T>{
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()

                    if (body!=null)
                        continuation.resume(body)
                    else
                        continuation.resumeWithException(RuntimeException("Response body is null"))
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }
}