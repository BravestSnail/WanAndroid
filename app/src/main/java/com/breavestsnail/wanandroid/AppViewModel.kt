package com.breavestsnail.wanandroid

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.breavestsnail.wanandroid.data.WebPage
import com.breavestsnail.wanandroid.logic.Repository
import com.breavestsnail.wanandroid.logic.ArticleDataSource
import com.breavestsnail.wanandroid.logic.ProjectArticleDataSource
import com.breavestsnail.wanandroid.logic.SearchDataSource
import com.breavestsnail.wanandroid.logic.dao.ChapterDao
import com.breavestsnail.wanandroid.logic.model.Banner
import com.breavestsnail.wanandroid.logic.model.Chapter
import com.breavestsnail.wanandroid.logic.model.User

class AppViewModel: ViewModel() {
    //根据cid获取文章列表，为null时返回所有文章
    fun getArticles(cid:Int?) = Pager(PagingConfig(pageSize = 1)){
        ArticleDataSource(cid)
    }.flow.cachedIn(viewModelScope)

    //    genBanner不需要参数，但是获取数据需要观察refreshLivedata的改变去获取数据，存入banner里
    private val refreshLiveData = MutableLiveData<Any?>()
    private fun getBanner(): LiveData<Result<List<Banner>>> {
        refreshLiveData.value = refreshLiveData.value
        return Transformations.switchMap(refreshLiveData){
            Repository.getBanner()
        }
    }
    val banners by lazy { getBanner() }

    //根据关键词搜索文章
    fun getSearchArticles(key:String) = Pager(PagingConfig(pageSize = 1)){
        SearchDataSource(key)
    }.flow.cachedIn(viewModelScope)

    //通过观察refreshLiveData1的变化，获取chpater数据
    private val refreshLiveData1 = MutableLiveData<Any?>()
    private fun getChapter(): LiveData<Result<List<Chapter>>> {
        refreshLiveData1.value = refreshLiveData1.value
        return Transformations.switchMap(refreshLiveData1){
            Repository.getChapter()
        }
    }
    val chapters by lazy { getChapter() }
    val firshChapter = Chapter(null,"最新文章")

    //通过观察refreshLiveData2的变化，projectChapters
    private val refreshLiveData2 = MutableLiveData<Any?>()
//    private fun getProjectChapter(): LiveData<Result<List<Chapter>>> {
//        refreshLiveData2.value = refreshLiveData2.value
//        return Transformations.switchMap(refreshLiveData2){
//            Log.d("test", "getProjectChapter:")
//            Repository.getProjectChapter()
//        }
//    }
//    val projectChapters by lazy { getProjectChapter() }

    val projectChapters by lazy {
        Repository.getProjectChapter()
        refreshLiveData2.value = refreshLiveData2.value
        Transformations.switchMap(refreshLiveData2){
            Repository.getProjectChapter()
        }
    }


    fun getProjectActicles(cid: Int) = Pager(PagingConfig(pageSize = 1)){
        ProjectArticleDataSource(cid)
    }.flow.cachedIn(viewModelScope)

    //存放每次点击Item的目的WebPage
    val acticleWebPage = MutableLiveData<WebPage>(WebPage("玩android","https://www.wanandroid.com/"))

    //将Chapters缓存到SharedPerferece中
    fun saveChapters(chapters: List<Chapter>,name:String) = Repository.saveChapters(chapters,name)

    //从SharedPerferece获取Chapters数据
    fun getSavedChapters(name:String) = Repository.getSavedChapters(name)

    //检查是否有缓存数据
    fun isChaptersSaved(name:String) = Repository.isChaptersSaved(name)

    //是否登录成功
    private val userLiveData = MutableLiveData<Pair<String,String>>()
    fun login(username:String,password:String): LiveData<Result<User>> {
        userLiveData.value = username to password
        return Transformations.switchMap(userLiveData){
            Repository.login(it.first,it.first)
        }
    }

}