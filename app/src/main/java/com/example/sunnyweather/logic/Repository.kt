package com.example.sunnyweather.logic

import androidx.lifecycle.liveData
import com.example.sunnyweather.logic.model.Place
import com.example.sunnyweather.logic.model.Weather
import com.example.sunnyweather.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.lang.RuntimeException


object Repository {
    fun searchPlaces(place: String) = liveData(Dispatchers.IO) {
        val result = try {
            val placeResponse = SunnyWeatherNetwork.searchPlace(place)
            if (placeResponse.status == "ok") {
                val places = placeResponse.places
                Result.success(places)
            } else {
                Result.failure(RuntimeException("Response status is ${placeResponse.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
        emit(result)
    }

    fun getRealTimeWeather(lng: String, lat: String) = liveData(Dispatchers.IO) {
        val result = try {
            val realTimeResponse = SunnyWeatherNetwork.getRealTimeWeather(lng, lat)
            if(realTimeResponse.status == "ok"){
                val realTimeInfo = realTimeResponse.result
                Result.success(realTimeInfo)
            }else{
                Result.failure(RuntimeException("Response status is ${realTimeResponse.status}"))
            }

        }catch (e: Exception) {
            Result.failure(e)
        }
        emit(result)
    }

    fun refreshWeather(lng: String, lat: String) = liveData(Dispatchers.IO) {
        val result = try {
            coroutineScope {
                val deferredRealTime = async {
                    SunnyWeatherNetwork.getRealTimeWeather(lng, lat)
                }
                val deferredDaily = async {
                    SunnyWeatherNetwork.getDailyWeather(lng, lat)
                }
                val realTimeInfo = deferredRealTime.await()
                val dailyInfo = deferredDaily.await()
                if(realTimeInfo.status == "ok" && dailyInfo.status == "ok"){
                    val weather = Weather(realTimeInfo.result.realTime, dailyInfo.result.daily)
                    Result.success(weather)
                }else{
                    Result.failure(RuntimeException(
                        "RealTimeResponse status is ${realTimeInfo.status} \n DailyResponse status is ${dailyInfo.status}"))
                }
            }
        }catch (e: Exception) {
            Result.failure(e)
        }
        emit(result)
    }
}