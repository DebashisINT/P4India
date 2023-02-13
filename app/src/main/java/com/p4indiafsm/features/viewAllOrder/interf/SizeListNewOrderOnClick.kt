package com.p4indiafsm.features.viewAllOrder.interf

import com.p4indiafsm.app.domain.NewOrderProductEntity
import com.p4indiafsm.app.domain.NewOrderSizeEntity

interface SizeListNewOrderOnClick {
    fun sizeListOnClick(size: NewOrderSizeEntity)
}