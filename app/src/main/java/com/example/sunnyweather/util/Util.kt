package com.example.sunnyweather.util

import android.widget.Toast
import com.example.sunnyweather.App

fun String.showToast(){
    Toast.makeText(App.context, this, Toast.LENGTH_SHORT).show()
}