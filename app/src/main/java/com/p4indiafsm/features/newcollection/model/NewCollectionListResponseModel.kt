package com.p4indiafsm.features.newcollection.model

import com.p4indiafsm.app.domain.CollectionDetailsEntity
import com.p4indiafsm.base.BaseResponse
import com.p4indiafsm.features.shopdetail.presentation.model.collectionlist.CollectionListDataModel

/**
 * Created by Saikat on 15-02-2019.
 */
class NewCollectionListResponseModel : BaseResponse() {
    //var collection_list: ArrayList<CollectionListDataModel>? = null
    var collection_list: ArrayList<CollectionDetailsEntity>? = null
}