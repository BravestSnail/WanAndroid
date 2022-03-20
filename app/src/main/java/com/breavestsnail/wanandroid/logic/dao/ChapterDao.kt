package com.breavestsnail.wanandroid.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.breavestsnail.wanandroid.WanAndroidApplication
import com.breavestsnail.wanandroid.logic.model.Chapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object ChapterDao {
    fun saveChapters(chapters: List<Chapter>,name:String){
        sharedPreferences().edit{
            putString(name,Gson().toJson(chapters))
        }
    }

    fun getSavedChapters(name: String):List<Chapter>{
        val chaptersJson = sharedPreferences().getString(name,"")
        return Gson().fromJson(chaptersJson, object : TypeToken<List<Chapter>>(){}.type)
    }

    fun isChaptersSaved(name: String) = sharedPreferences().contains(name)

    private fun sharedPreferences() = WanAndroidApplication.context
        .getSharedPreferences("wan_android",Context.MODE_PRIVATE)
}