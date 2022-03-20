package com.breavestsnail.wanandroid

import java.lang.IllegalArgumentException

enum class WanAndroidScreen(val desc:String,val icon: Int) {
    Home("首页",R.drawable.ic_home),
    Project( "项目",R.drawable.ic_project),
//    Question("问答",R.drawable.ic_question),
    Me("我",R.drawable.ic_me);

    companion object{
        fun fromRoute(route:String?):WanAndroidScreen =
            when(route?.substringBefore("/")){
                Home.name -> Home
                Project.name -> Project
//                Question.name -> Question
                Me.name -> Me
                null -> Home
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
    }
}

