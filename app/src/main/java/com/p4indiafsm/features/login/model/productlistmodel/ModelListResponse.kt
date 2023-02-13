package com.p4indiafsm.features.login.model.productlistmodel

import com.p4indiafsm.app.domain.ModelEntity
import com.p4indiafsm.app.domain.ProductListEntity
import com.p4indiafsm.base.BaseResponse

class ModelListResponse: BaseResponse() {
    var model_list: ArrayList<ModelEntity>? = null
}