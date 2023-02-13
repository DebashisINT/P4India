package com.p4indiafsm.features.weather.api

import com.p4indiafsm.features.task.api.TaskApi
import com.p4indiafsm.features.task.api.TaskRepo

object WeatherRepoProvider {
    fun weatherRepoProvider(): WeatherRepo {
        return WeatherRepo(WeatherApi.create())
    }
}