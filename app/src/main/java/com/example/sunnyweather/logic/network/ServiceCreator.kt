package com.example.sunnyweather.logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/*
一个服务单例类，对于不同类型的请求接口类型创建不同的retrofit构建器
 */
object ServiceCreator {
    private const val BASE_URL = "https://api.caiyun.com/"

    private val retrofit = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()

    fun<T> create(serviceClass: Class<T>) = retrofit.create(serviceClass)

    //另外一种方法
    inline fun<reified T>create() = create(T::class.java)
}