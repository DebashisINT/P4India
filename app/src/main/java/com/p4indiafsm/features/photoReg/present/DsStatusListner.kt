package com.p4indiafsm.features.photoReg.present

import com.p4indiafsm.app.domain.ProspectEntity
import com.p4indiafsm.features.photoReg.model.UserListResponseModel

interface DsStatusListner {
    fun getDSInfoOnLick(obj: ProspectEntity)
}