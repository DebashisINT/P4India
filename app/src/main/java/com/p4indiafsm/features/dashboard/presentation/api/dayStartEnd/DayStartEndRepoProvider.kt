package com.p4indiafsm.features.dashboard.presentation.api.dayStartEnd

import com.p4indiafsm.features.stockCompetetorStock.api.AddCompStockApi
import com.p4indiafsm.features.stockCompetetorStock.api.AddCompStockRepository

object DayStartEndRepoProvider {
    fun dayStartRepositiry(): DayStartEndRepository {
        return DayStartEndRepository(DayStartEndApi.create())
    }

}