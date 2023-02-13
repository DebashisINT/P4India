package com.p4indiafsm.features.task.api

import com.p4indiafsm.features.timesheet.api.TimeSheetApi
import com.p4indiafsm.features.timesheet.api.TimeSheetRepo

/**
 * Created by Saikat on 12-Aug-20.
 */
object TaskRepoProvider {
    fun taskRepoProvider(): TaskRepo {
        return TaskRepo(TaskApi.create())
    }
}