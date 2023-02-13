package com.p4indiafsm.features.login.model.productlistmodel

import com.p4indiafsm.app.domain.ProductListEntity
import com.p4indiafsm.base.BaseResponse

/**
 * Created by Saikat on 20-11-2018.
 */
class ProductListResponseModel : BaseResponse() {
    //var product_list: ArrayList<ProductListDataModel>? = null
    var product_list: ArrayList<ProductListEntity>? = null
}