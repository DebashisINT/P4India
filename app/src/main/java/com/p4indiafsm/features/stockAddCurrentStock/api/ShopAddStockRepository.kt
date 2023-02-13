package com.p4indiafsm.features.stockAddCurrentStock.api

import com.p4indiafsm.base.BaseResponse
import com.p4indiafsm.features.location.model.ShopRevisitStatusRequest
import com.p4indiafsm.features.location.shopRevisitStatus.ShopRevisitStatusApi
import com.p4indiafsm.features.stockAddCurrentStock.ShopAddCurrentStockRequest
import com.p4indiafsm.features.stockAddCurrentStock.model.CurrentStockGetData
import com.p4indiafsm.features.stockCompetetorStock.model.CompetetorStockGetData
import io.reactivex.Observable

class ShopAddStockRepository (val apiService : ShopAddStockApi){
    fun shopAddStock(shopAddCurrentStockRequest: ShopAddCurrentStockRequest?): Observable<BaseResponse> {
        return apiService.submShopAddStock(shopAddCurrentStockRequest)
    }

    fun getCurrStockList(sessiontoken: String, user_id: String, date: String): Observable<CurrentStockGetData> {
        return apiService.getCurrStockListApi(sessiontoken, user_id, date)
    }

}