package com.p4indiafsm.features.login.api.alarmconfigapi

/**
 * Created by Saikat on 19-02-2019.
 */
object AlarmConfigRepoProvider {
    fun provideAlarmConfigRepository(): AlarmConfigRepo {
        return AlarmConfigRepo(AlarmConfigApi.create())
    }
}