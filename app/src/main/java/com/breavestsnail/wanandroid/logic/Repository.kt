package com.breavestsnail.wanandroid.logic

import androidx.lifecycle.liveData
import com.breavestsnail.wanandroid.logic.dao.ChapterDao
import com.breavestsnail.wanandroid.logic.model.Chapter
import com.breavestsnail.wanandroid.logic.model.User
import com.breavestsnail.wanandroid.logic.network.AppNetwork
import kotlinx.coroutines.Dispatchers
import java.lang.RuntimeException
import kotlin.coroutines.CoroutineContext

object Repository {
    suspend fun getActicle(page:Int,cid:Int?) = AppNetwork.getArticle(page,cid)

    suspend fun getSearchList(page: Int,key:String) = AppNetwork.getSearchList(page, key)

    fun saveChapters(chapters: List<Chapter>,name:String) = ChapterDao.saveChapters(chapters,name)

    fun getSavedChapters(name:String) = ChapterDao.getSavedChapters(name)

    fun isChaptersSaved(name:String) = ChapterDao.isChaptersSaved(name)

    fun getTopActicle() = fire(Dispatchers.IO){
        val articleResponse = AppNetwork.getTopActicle()
        if (articleResponse.errorCode==0){
            Result.success(articleResponse.data)
        }else{
            Result.failure(RuntimeException("response status is ${articleResponse.errorCode}"))
        }
    }

    fun getBanner() = fire(Dispatchers.IO){
        val bannerResponse = AppNetwork.getBanner()
        if (bannerResponse.errorCode==0){
            val data = bannerResponse.data
            Result.success(data)
        }else{
            Result.failure(RuntimeException("response status is ${bannerResponse.errorCode}"))
        }
    }

    fun getChapter() = fire(Dispatchers.IO){
        val chapterResponse = AppNetwork.getChapter()
        if (chapterResponse.errorCode==0){
            val data = chapterResponse.data
            Result.success(data)
        }else{
            Result.failure(RuntimeException("response status is ${chapterResponse.errorCode}"))
        }
    }

    fun getProjectChapter() = fire(Dispatchers.IO){
        val chapterResponse = AppNetwork.getProjectChapter()
        if (chapterResponse.errorCode==0){
            val data = chapterResponse.data
            Result.success(data)
        }else{
            Result.failure(RuntimeException("response status is ${chapterResponse.errorCode}"))
        }
    }

    suspend fun getProjectArticle(page: Int,cid: Int?) = AppNetwork.getProjectActicle(page, cid)

    fun login(username:String,password:String) = fire(Dispatchers.IO){
        val response = AppNetwork.login(username, password)
        if (response.errorCode==0){
            Result.success(response.data)
        }else{
            Result.success(User(-1,false, listOf(), listOf(),"","","",""))
        }
    }

    //    统一入口封装网络请求函数
    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) = liveData<Result<T>> {
        val result = try {
            block()
        } catch (e: Exception){
            Result.failure<T>(e)
        }
        emit(result)
    }
}