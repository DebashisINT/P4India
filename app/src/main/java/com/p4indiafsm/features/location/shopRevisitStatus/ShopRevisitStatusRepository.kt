package com.p4indiafsm.features.location.shopRevisitStatus

import com.p4indiafsm.base.BaseResponse
import com.p4indiafsm.features.location.model.ShopDurationRequest
import com.p4indiafsm.features.location.model.ShopRevisitStatusRequest
import io.reactivex.Observable

class ShopRevisitStatusRepository(val apiService : ShopRevisitStatusApi) {
    fun shopRevisitStatus(shopRevisitStatus: ShopRevisitStatusRequest?): Observable<BaseResponse> {
        return apiService.submShopRevisitStatus(shopRevisitStatus)
    }
}