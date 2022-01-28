package com.example.sunnyweather.logic.network

import android.annotation.SuppressLint
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


object SunnyWeatherNetwork {
    const val SP_TAG = "NET_WORK_REQUEST_SEARCH_PLACE"
    const val RR_TAG = "NET_WORK_REQUEST_REALTIME_REQUEST"
    const val DR_TAG = "NET_WORK_REQUEST_DAILY_REQUEST"
    //获取位置信息
    private val placeService = ServiceCreator.create(PlaceService::class.java)

    //获取实时天气预报
    private val weatherService = ServiceCreator.create<WeatherService>()

    /*
    请求不同城市的位置
     */
    suspend fun searchPlace(query: String) = placeService.searchPlaces(query).await()

    /*
    请求精确地点的实时天气以
     */
    suspend fun getRealTimeWeather(lng: String, lat: String) = weatherService.getRealTimeWeather(lng, lat).await()

    /*
    请求未来几天的天气
     */
    suspend fun getDailyWeather(lng: String, lat: String) = weatherService.getDailyWeather(lng, lat).await()


    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                @SuppressLint("LongLogTag")
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    Log.d(SP_TAG, "onResponse:retrofit has return right respond")
                    val body = response.body()
                    Log.d(SP_TAG, "onResponse:retrofit body is $body")

                    if (body != null) {
                        continuation.resume(body)
                    } else {
                        RuntimeException("respone body is null")
                    }
                }

                @SuppressLint("LongLogTag")
                override fun onFailure(call: Call<T>, t: Throwable) {
                    Log.d(SP_TAG, "onResponse:retrofit has return false respond")
                    continuation.resumeWithException(t)
                }
            })
        }
    }
}