package com.p4indiafsm.features.orderList.model

import com.p4indiafsm.base.BaseResponse


class ReturnListResponseModel: BaseResponse() {
    var return_list: ArrayList<ReturnDataModel>? = null
}