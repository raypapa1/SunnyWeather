package com.example.sunnyweather.logic.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.sunnyweather.logic.Repository

class WeatherModel : ViewModel() {
    private val locationLivedata = MutableLiveData<Location>()

    var locationLat = ""
    var locationLng = ""
    var placeName = ""

    val weatherLiveData = Transformations.switchMap(locationLivedata){
        Repository.refreshWeather(it.lng, it.lat)
    }

    fun refreshWeather(lng: String, lat:String){
        locationLivedata.value = Location(lng, lat)
    }
}