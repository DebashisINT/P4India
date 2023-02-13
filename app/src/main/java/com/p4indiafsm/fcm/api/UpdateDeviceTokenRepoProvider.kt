package com.p4indiafsm.fcm.api

/**
 * Created by Saikat on 27-02-2019.
 */
object UpdateDeviceTokenRepoProvider {
    fun updateDeviceTokenRepoProvider(): UpdateDeviceTokenRepo {
        return UpdateDeviceTokenRepo(UpdateDeviceTokenApi.create())
    }
}