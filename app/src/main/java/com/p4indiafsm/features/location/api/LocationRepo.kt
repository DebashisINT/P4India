package com.p4indiafsm.features.location.api

import com.p4indiafsm.app.Pref
import com.p4indiafsm.base.BaseResponse
import com.p4indiafsm.features.location.model.AppInfoInputModel
import com.p4indiafsm.features.location.model.AppInfoResponseModel
import com.p4indiafsm.features.location.model.GpsNetInputModel
import com.p4indiafsm.features.location.model.ShopDurationRequest
import com.p4indiafsm.features.location.shopdurationapi.ShopDurationApi
import io.reactivex.Observable

/**
 * Created by Saikat on 17-Aug-20.
 */
class LocationRepo(val apiService: LocationApi) {
    fun appInfo(appInfo: AppInfoInputModel?): Observable<BaseResponse> {
        return apiService.submitAppInfo(appInfo)
    }

    fun getAppInfo(): Observable<AppInfoResponseModel> {
        return apiService.getAppInfo(Pref.session_token!!, Pref.user_id!!)
    }

    fun gpsNetInfo(appInfo: GpsNetInputModel?): Observable<BaseResponse> {
        return apiService.submitGpsNetInfo(appInfo)
    }
}