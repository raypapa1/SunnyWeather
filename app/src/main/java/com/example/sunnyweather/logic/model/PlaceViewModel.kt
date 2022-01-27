package com.example.sunnyweather.logic.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.sunnyweather.logic.Repository

class PlaceViewModel: ViewModel() {

    private val searchLiveData = MutableLiveData<String>()

    val placeList = ArrayList<Place>()

    val placeLivedata = Transformations.switchMap(searchLiveData){
        Repository.searchPlaces(it)
    }

    fun searchPlaces(query: String){
        searchLiveData.value = query
    }
}