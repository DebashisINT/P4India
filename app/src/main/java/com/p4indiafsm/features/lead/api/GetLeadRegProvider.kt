package com.p4indiafsm.features.lead.api

import com.p4indiafsm.features.NewQuotation.api.GetQuotListRegRepository
import com.p4indiafsm.features.NewQuotation.api.GetQutoListApi


object GetLeadRegProvider {
    fun provideList(): GetLeadListRegRepository {
        return GetLeadListRegRepository(GetLeadListApi.create())
    }
}