package com.p4indiafsm.features.micro_learning.presentation

import android.app.IntentService
import android.content.Intent
import com.p4indiafsm.R
import com.p4indiafsm.app.NetworkConstant
import com.p4indiafsm.app.Pref
import com.p4indiafsm.app.utils.AppUtils
import com.p4indiafsm.app.utils.Toaster
import com.p4indiafsm.base.BaseResponse
import com.p4indiafsm.base.presentation.BaseActivity
import com.p4indiafsm.features.dashboard.presentation.DashboardActivity
import com.p4indiafsm.features.micro_learning.api.MicroLearningRepoProvider
import com.elvishew.xlog.XLog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class FileOpeningTimeIntentService : IntentService("") {

    override fun onHandleIntent(p0: Intent?) {

        val id = p0?.getStringExtra("id")
        val startTime = p0?.getStringExtra("start_time")

        val repository = MicroLearningRepoProvider.microLearningRepoProvider()
        BaseActivity.compositeDisposable.add(
                repository.updateFileOpeningTime(id!!, startTime!!)
                        .subscribe({ result ->
                            val response = result as BaseResponse
                            XLog.d("UPDATE FILE OPENING TIME: " + "RESPONSE : " + response.status + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name + ",MESSAGE : " + response.message)
                        }, { error ->
                            XLog.d("UPDATE FILE OPENING TIME: " + "ERROR : " + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name + ",MESSAGE : " + error.localizedMessage)
                            error.printStackTrace()
                        })
        )
    }
}