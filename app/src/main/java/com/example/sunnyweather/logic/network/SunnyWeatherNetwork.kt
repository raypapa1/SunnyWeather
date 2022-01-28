package com.example.sunnyweather.logic.network

import android.annotation.SuppressLint
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


object SunnyWeatherNetwork {
    const val SP_TAG = "NET_WORK_REQUEST_SEARCH_PLACE"
    private val placeService = ServiceCreator.create(PlaceService::class.java)

    //开始请求方位
    suspend fun searchPlace(query: String) = placeService.searchPlaces(query).await()


    //调用searchPlace后会执行await()函数，如果返回的数据是正确的就执行。不正确就抛出异常
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