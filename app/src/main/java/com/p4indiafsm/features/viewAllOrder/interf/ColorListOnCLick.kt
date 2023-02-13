package com.p4indiafsm.features.viewAllOrder.interf

import com.p4indiafsm.app.domain.NewOrderGenderEntity
import com.p4indiafsm.features.viewAllOrder.model.ProductOrder

interface ColorListOnCLick {
    fun colorListOnCLick(size_qty_list: ArrayList<ProductOrder>, adpPosition:Int)
}