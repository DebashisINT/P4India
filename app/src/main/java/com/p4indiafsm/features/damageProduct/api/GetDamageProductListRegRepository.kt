package com.p4indiafsm.features.damageProduct.api

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import com.p4indiafsm.app.FileUtils
import com.p4indiafsm.base.BaseResponse
import com.p4indiafsm.features.NewQuotation.model.*
import com.p4indiafsm.features.addshop.model.AddShopRequestData
import com.p4indiafsm.features.addshop.model.AddShopResponse
import com.p4indiafsm.features.damageProduct.model.DamageProductResponseModel
import com.p4indiafsm.features.damageProduct.model.delBreakageReq
import com.p4indiafsm.features.damageProduct.model.viewAllBreakageReq
import com.p4indiafsm.features.login.model.userconfig.UserConfigResponseModel
import com.p4indiafsm.features.myjobs.model.WIPImageSubmit
import com.p4indiafsm.features.photoReg.model.*
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class GetDamageProductListRegRepository(val apiService : GetDamageProductListApi) {

    fun viewBreakage(req: viewAllBreakageReq): Observable<DamageProductResponseModel> {
        return apiService.viewBreakage(req)
    }

    fun delBreakage(req: delBreakageReq): Observable<BaseResponse>{
        return apiService.BreakageDel(req.user_id!!,req.breakage_number!!,req.session_token!!)
    }

}