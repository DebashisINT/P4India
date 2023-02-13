package com.p4indiafsm.features.login.model.productlistmodel

import com.p4indiafsm.app.domain.NewOrderScrOrderEntity
import com.p4indiafsm.app.domain.ProductListEntity

class NewOdrScrOrderListModel {
    var status:String? = null
    var message:String? = null
    var user_id:String? = null
    var order_list: ArrayList<NewOrderScrOrderEntity>? = null
}