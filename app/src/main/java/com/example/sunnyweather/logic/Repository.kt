package com.example.sunnyweather.logic

import android.util.Log
import androidx.lifecycle.liveData
import com.example.sunnyweather.logic.model.Place
import com.example.sunnyweather.logic.model.Weather
import com.example.sunnyweather.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.lang.RuntimeException
import kotlin.coroutines.CoroutineContext


object Repository {
    private const val TAG = "REPOSITORY REQUEST"

    fun searchPlaces(place: String) = fire(Dispatchers.IO) {
        coroutineScope {
            val placeResponse = SunnyWeatherNetwork.searchPlace(place)
            if (placeResponse.status == "ok") {
                val places = placeResponse.places
                Result.success(places)
            } else {
                Result.failure(RuntimeException("Response status is ${placeResponse.status}"))
            }
        }
    }


    fun refreshWeather(lng: String, lat: String) = fire(Dispatchers.IO) {
        coroutineScope {
            Log.d( TAG, "getRealTimeWeather:begin Get Data")
            val deferredRealTime = async {
                SunnyWeatherNetwork.getRealTimeWeather(lng, lat)
            }
            val deferredDaily = async {
                SunnyWeatherNetwork.getDailyWeather(lng, lat)
            }
            val realTimeInfo = deferredRealTime.await()
            Log.d( TAG, "getRealTimeWeather: ${realTimeInfo.status}-${realTimeInfo.result.realTime.airQuality.aqi.chn}")
            val dailyInfo = deferredDaily.await()
            Log.d( TAG, "getRealTimeWeather: ${dailyInfo.status}-${dailyInfo.result.daily.skycon}")

            if (realTimeInfo.status == "ok" && dailyInfo.status == "ok") {
                val weather = Weather(realTimeInfo.result.realTime, dailyInfo.result.daily)
                Result.success(weather)
            } else {
                Result.failure(
                    RuntimeException(
                        "RealTimeResponse status is ${realTimeInfo.status} \n DailyResponse status is ${dailyInfo.status}"
                    )
                )
            }
        }
    }

    /*
    封装try-catch
     */
    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure(e)
            }
            emit(result)
        }
}