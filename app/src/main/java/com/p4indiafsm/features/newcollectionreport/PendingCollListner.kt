package com.p4indiafsm.features.newcollectionreport

import com.p4indiafsm.features.photoReg.model.UserListResponseModel

interface PendingCollListner {
    fun getUserInfoOnLick(obj: PendingCollData)
}