package com.p4indiafsm.features.weather.api

import com.p4indiafsm.base.BaseResponse
import com.p4indiafsm.features.task.api.TaskApi
import com.p4indiafsm.features.task.model.AddTaskInputModel
import com.p4indiafsm.features.weather.model.ForeCastAPIResponse
import com.p4indiafsm.features.weather.model.WeatherAPIResponse
import io.reactivex.Observable

class WeatherRepo(val apiService: WeatherApi) {
    fun getCurrentWeather(zipCode: String): Observable<WeatherAPIResponse> {
        return apiService.getTodayWeather(zipCode)
    }

    fun getWeatherForecast(zipCode: String): Observable<ForeCastAPIResponse> {
        return apiService.getForecast(zipCode)
    }
}