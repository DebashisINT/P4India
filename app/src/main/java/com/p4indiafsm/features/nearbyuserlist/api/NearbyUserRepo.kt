package com.p4indiafsm.features.nearbyuserlist.api

import com.p4indiafsm.app.Pref
import com.p4indiafsm.features.nearbyuserlist.model.NearbyUserResponseModel
import com.p4indiafsm.features.newcollection.model.NewCollectionListResponseModel
import com.p4indiafsm.features.newcollection.newcollectionlistapi.NewCollectionListApi
import io.reactivex.Observable

class NearbyUserRepo(val apiService: NearbyUserApi) {
    fun nearbyUserList(): Observable<NearbyUserResponseModel> {
        return apiService.getNearbyUserList(Pref.session_token!!, Pref.user_id!!)
    }
}