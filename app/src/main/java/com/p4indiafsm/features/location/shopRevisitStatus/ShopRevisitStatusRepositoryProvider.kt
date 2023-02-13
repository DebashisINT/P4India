package com.p4indiafsm.features.location.shopRevisitStatus

import com.p4indiafsm.features.location.shopdurationapi.ShopDurationApi
import com.p4indiafsm.features.location.shopdurationapi.ShopDurationRepository

object ShopRevisitStatusRepositoryProvider {
    fun provideShopRevisitStatusRepository(): ShopRevisitStatusRepository {
        return ShopRevisitStatusRepository(ShopRevisitStatusApi.create())
    }
}