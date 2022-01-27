package com.example.sunnyweather

import android.app.Application
import android.content.Context

class App: Application() {
    companion object{
        @SuppressWarnings("StaticFieldLeak")
        lateinit var context: Context

        //令牌值
        const val TOKEN = "jnB9ZYxgej0ZXGbt"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}