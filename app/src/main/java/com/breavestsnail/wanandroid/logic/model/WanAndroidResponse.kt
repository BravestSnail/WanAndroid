package com.breavestsnail.wanandroid.logic.model

import com.breavestsnail.wanandroid.data.TabData
import com.google.gson.annotations.SerializedName

//玩android网站的返回数据模型
data class WanAndroidResponse<T>(val data: T, val errorCode:Int,val errorMsg:String)

//一页文章的数据模型
data class ArticleData(val curPage:Int, @SerializedName("datas")val articles: List<Article>, val pageCount: Int)

//一篇文章的数据模型
data class Article(val author:String?,val chapterName:String,val envelopePic:String?,val shareUser:String?,val link:String, val niceDate:String,val superChapterName:String?,val title:String)

//横幅广告的数据模型
data class Banner(val desc:String,val id:Int,val imagePath:String, val title:String,val url:String)

//文章章节的数据模型
data class Chapter(@SerializedName("id")val cid: Int?,@SerializedName("name") override val name:String):TabData

//返回用户数据
data class User(val id: Int, val admin: Boolean, val chapterTops: List<String>, val collectIds: List<Int>, val email: String, val icon: String, val nickname: String, val username: String,)
