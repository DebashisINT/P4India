package com.p4indiafsm.features.viewAllOrder.interf

import com.p4indiafsm.app.domain.NewOrderGenderEntity
import com.p4indiafsm.app.domain.NewOrderProductEntity

interface ProductListNewOrderOnClick {
    fun productListOnClick(product: NewOrderProductEntity)
}