package com.p4indiafsm.features.menuBeat

import com.p4indiafsm.app.NetworkConstant
import com.p4indiafsm.base.BaseResponse
import com.p4indiafsm.features.addshop.model.AddQuestionSubmitRequestData
import com.p4indiafsm.features.addshop.model.AddShopRequestData
import com.p4indiafsm.features.addshop.model.AddShopResponse
import com.p4indiafsm.features.addshop.model.imageListResponse
import com.p4indiafsm.features.addshop.presentation.ShopListSubmitResponse
import com.p4indiafsm.features.addshop.presentation.multiContactRequestData
import com.p4indiafsm.features.beatCustom.BeatGetStatusModel
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

/**
 * Created by Created by saheli on 16-12-2023.
 */
interface MenuBeatApi {
    @FormUrlEncoded
    @POST("AreaRouteBeatRelationInfo/AreaRouteBeatList")
    fun getCurrentTabData(@Field("user_id") user_id: String,@Field("session_token") session_token: String,@Field("beat_id") beat_id: String):
            Observable<MenuBeatResponse>

    @FormUrlEncoded
    @POST("AreaRouteBeatRelationInfo/AreaRouteBeatDetailsList")
    fun getHirerchyTabData(@Field("user_id") user_id: String,@Field("session_token") session_token: String):
            Observable<MenuBeatAreaRouteResponse>


    /**
     * Companion object to create the GithubApiService
     */
    companion object Factory {
        fun create(): MenuBeatApi {
            val retrofit = Retrofit.Builder()
                    .client(NetworkConstant.setTimeOut())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(NetworkConstant.ADD_SHOP_BASE_URL)
                    .build()

            return retrofit.create(MenuBeatApi::class.java)
        }

        fun createWithoutMultipart(): MenuBeatApi {
            val retrofit = Retrofit.Builder()
                    .client(NetworkConstant.setTimeOut())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(NetworkConstant.BASE_URL)
                    .build()

            return retrofit.create(MenuBeatApi::class.java)
        }
    }
}