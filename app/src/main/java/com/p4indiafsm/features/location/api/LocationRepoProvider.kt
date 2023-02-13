package com.p4indiafsm.features.location.api

import com.p4indiafsm.features.location.shopdurationapi.ShopDurationApi
import com.p4indiafsm.features.location.shopdurationapi.ShopDurationRepository


object LocationRepoProvider {
    fun provideLocationRepository(): LocationRepo {
        return LocationRepo(LocationApi.create())
    }
}