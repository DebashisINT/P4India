package com.p4indiafsm.features.viewAllOrder.interf

import com.p4indiafsm.app.domain.NewOrderGenderEntity
import com.p4indiafsm.features.viewAllOrder.model.ProductOrder
import java.text.FieldPosition

interface NewOrderSizeQtyDelOnClick {
    fun sizeQtySelListOnClick(product_size_qty: ArrayList<ProductOrder>)
    fun sizeQtyListOnClick(product_size_qty: ProductOrder,position: Int)
}