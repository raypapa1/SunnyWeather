package com.example.sunnyweather.logic.network

import com.example.sunnyweather.App
import com.example.sunnyweather.logic.model.DailyResponse
import com.example.sunnyweather.logic.model.RealTimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherService {
    @GET("v2.5/${App.TOKEN}/{lng},{lat}/realtime.json")
    fun getRealTimeWeather(@Path("lng")lng: String, @Path("lat")lat: String): Call<RealTimeResponse>

    @GET("v2.5/${App.TOKEN}/{lng},{lat}/daily.json")
    fun getDailyWeather(@Path("lng")lng: String, @Path("lat")lat: String): Call<DailyResponse>
}