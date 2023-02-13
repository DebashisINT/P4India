package com.p4indiafsm.features.stock.api

import com.p4indiafsm.app.Pref
import com.p4indiafsm.base.BaseResponse
import com.p4indiafsm.features.stock.model.AddStockInputParamsModel
import com.p4indiafsm.features.stock.model.NewStockListResponseModel
import io.reactivex.Observable

/**
 * Created by Saikat on 17-09-2019.
 */
class StockRepository(val apiService: StockApi) {

    fun addStock(addStock: AddStockInputParamsModel): Observable<BaseResponse> {
        return apiService.addStock(addStock)
    }

    fun getStockList(): Observable<NewStockListResponseModel> {
        return apiService.getStockList(Pref.session_token!!, Pref.user_id!!)
    }
}