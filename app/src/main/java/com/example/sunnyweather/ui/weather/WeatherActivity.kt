package com.example.sunnyweather.ui.weather

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.sunnyweather.R
import com.example.sunnyweather.databinding.ActivityWeatherBinding
import com.example.sunnyweather.databinding.ForecastBinding
import com.example.sunnyweather.databinding.LifeIndexBinding
import com.example.sunnyweather.databinding.NowBinding
import com.example.sunnyweather.logic.model.Weather
import com.example.sunnyweather.logic.model.WeatherModel
import com.example.sunnyweather.logic.model.getSky
import com.example.sunnyweather.util.showToast
import java.text.SimpleDateFormat
import java.util.*

class WeatherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWeatherBinding

    val model by lazy { ViewModelProvider(this).get(WeatherModel::class.java) }

    @SuppressLint("LongLogTag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (model.locationLng.isEmpty()) {
            model.locationLng = intent.getStringExtra("location_lng") ?: ""
            Log.d("OnWeatherActivityCreate:", model.locationLng)
        }
        if (model.locationLat.isEmpty()) {
            model.locationLat = intent.getStringExtra("location_lat") ?: ""
            Log.d("OnWeatherActivityCreate:", model.locationLat)
        }
        if (model.placeName.isEmpty()) {
            model.placeName = intent.getStringExtra("place_name") ?: ""
            Log.d("OnWeatherActivityCreate:", model.placeName)

        }
        model.weatherLiveData.observe(this) {
            val weather = it.getOrNull()
            if (weather != null) {
                Log.d("OnWeatherActivityCreate:", "return data is not null and show it ")
                showWeatherInfo(weather)
            } else {
                Log.d("OnWeatherActivityCreate:", "return data is null ")
                "无法成功获取天气信息".showToast()
                it.exceptionOrNull()?.printStackTrace()
            }
        }
        Log.d("OnWeatherActivityCreate:","begin refresh weather")
        model.refreshWeather(model.locationLng, model.locationLat)

    }


    @SuppressLint("SetTextI18n", "LongLogTag")
    private fun showWeatherInfo(weather: Weather) {
        Log.d("showWeatherInfo:", weather.toString())
        binding.nowLayout.placeName.text = model.placeName
        val realTime = weather.realTime
        val daily = weather.daily

        //填充now中的布局
        binding.nowLayout.currentTemp.text = "${realTime.temperature.toInt()}℃"
        binding.nowLayout.currentSky.text = getSky(realTime.skycon).info
        binding.nowLayout.currentAQI.text = "空气指数${realTime.airQuality.aqi.chn.toInt()}"
        binding.nowLayout.nowLayout.setBackgroundResource(getSky(realTime.skycon).bg)

        //填充forecast中的数据,先清除后添加
        binding.forecastLayout.forecastLayout.removeAllViews()
        val days = daily.skycon.size
        for (i in 0 until days) {
            val skycon = daily.skycon[i]
            val temperature = daily.temperature[i]
            val view = LayoutInflater.from(this)
                .inflate(R.layout.forecast_item, binding.forecastLayout.forecastLayout, false)
            val dateInfo = view.findViewById(R.id.dateInfo) as TextView
            val skyIcon = view.findViewById(R.id.skyIcon) as ImageView
            val skyInfo = view.findViewById(R.id.skyInfo) as TextView
            val temperatureInfo = view.findViewById(R.id.temperatureInfo) as TextView

            //格式化时间
            val simpleDateFormat = SimpleDateFormat("yyyy-mm-dd", Locale.getDefault())
            dateInfo.text = skycon.date
            Log.d("showWeatherInfo:", "${dateInfo.text}\n")
            val sky = getSky(skycon.value)
            skyIcon.setImageResource(sky.icon)
            skyInfo.text = sky.info
            temperatureInfo.text = "${temperature.min.toInt()} ~ ${temperature.max.toInt()}℃"
            binding.forecastLayout.forecastLayout.addView(view)
        }

        //填充life_index布局中的数据
        val lifeIndex = daily.lifeIndex
        binding.lifeLayout.coldRiskText.text = lifeIndex.coldRisk[0].desc
        binding.lifeLayout.dressingText.text = lifeIndex.dressing[0].desc
        binding.lifeLayout.ultravioletText.text = lifeIndex.ultraviolet[0].desc
        binding.lifeLayout.carWashingText.text = lifeIndex.carWashing[0].desc

        Log.d("showWeatherInfo:", "set weatherLayout Visible")
        binding.weatherLayout.visibility = View.VISIBLE
    }
}