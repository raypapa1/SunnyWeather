package com.example.sunnyweather.logic.model

import com.google.gson.annotations.SerializedName

data class RealTimeResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("result")
    val result: Result
)

data class Result(
    @SerializedName("realtime")
    val realTime: RealTime,
    )

data class RealTime(
    @SerializedName("temperature")
    val temperature: Float,
    @SerializedName("humidity")
    val humidity: Float,
    @SerializedName("skycon")
    val skycon: String,
    @SerializedName("visibility")
    val visibility: Float,
    @SerializedName("air_quality")
    val airQuality: AirQuality
)
data class AirQuality(
    @SerializedName("aqi")
    val aqi: Aqi
)

data class Aqi(
    val chn: Float,
    val usa: Float
)
