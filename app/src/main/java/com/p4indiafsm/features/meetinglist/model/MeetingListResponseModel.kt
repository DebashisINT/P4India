package com.p4indiafsm.features.meetinglist.model

import com.p4indiafsm.base.BaseResponse
import com.p4indiafsm.features.location.model.MeetingDurationDataModel
import java.io.Serializable

/**
 * Created by Saikat on 21-01-2020.
 */
class MeetingListResponseModel : BaseResponse(), Serializable {
    var meeting_list: ArrayList<MeetingDurationDataModel>? = null
}