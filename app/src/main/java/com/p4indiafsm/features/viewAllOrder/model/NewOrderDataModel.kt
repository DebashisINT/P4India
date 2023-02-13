package com.p4indiafsm.features.viewAllOrder.model

import com.p4indiafsm.app.domain.NewOrderColorEntity
import com.p4indiafsm.app.domain.NewOrderGenderEntity
import com.p4indiafsm.app.domain.NewOrderProductEntity
import com.p4indiafsm.app.domain.NewOrderSizeEntity
import com.p4indiafsm.features.stockCompetetorStock.model.CompetetorStockGetDataDtls

class NewOrderDataModel {
    var status:String ? = null
    var message:String ? = null
    var Gender_list :ArrayList<NewOrderGenderEntity>? = null
    var Product_list :ArrayList<NewOrderProductEntity>? = null
    var Color_list :ArrayList<NewOrderColorEntity>? = null
    var size_list :ArrayList<NewOrderSizeEntity>? = null
}

