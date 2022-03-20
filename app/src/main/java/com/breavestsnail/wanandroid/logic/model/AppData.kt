package com.breavestsnail.wanandroid.data

import androidx.compose.runtime.Immutable
import com.breavestsnail.wanandroid.R
import com.breavestsnail.wanandroid.logic.model.Chapter

interface TabData{
    val name:String
}

@Immutable
data class WebPage(val title:String,val url:String)


@Immutable
data class Function(val title: String,val icon: Int)

object AppData{
    val functions = listOf<Function>(
        Function("我的收藏", R.drawable.ic_collect),
        Function("积分排行", R.drawable.ic_rank),
        Function("设置", R.drawable.ic_set)
    )
}