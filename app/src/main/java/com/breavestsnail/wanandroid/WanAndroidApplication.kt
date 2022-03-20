package com.breavestsnail.wanandroid

import android.app.Application
import android.content.Context

class WanAndroidApplication : Application() {
    companion object{
        @SuppressWarnings("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}