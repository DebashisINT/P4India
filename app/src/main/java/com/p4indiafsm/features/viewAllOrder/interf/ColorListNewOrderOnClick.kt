package com.p4indiafsm.features.viewAllOrder.interf

import com.p4indiafsm.app.domain.NewOrderColorEntity
import com.p4indiafsm.app.domain.NewOrderProductEntity

interface ColorListNewOrderOnClick {
    fun productListOnClick(color: NewOrderColorEntity)
}