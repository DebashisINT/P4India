package com.p4indiafsm.features.chat.presentation

import android.content.Context
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.p4indiafsm.R
import com.p4indiafsm.app.NetworkConstant
import com.p4indiafsm.app.SearchListener
import com.p4indiafsm.app.utils.AppUtils
import com.p4indiafsm.base.BaseResponse
import com.p4indiafsm.base.presentation.BaseActivity
import com.p4indiafsm.base.presentation.BaseFragment
import com.p4indiafsm.features.chat.api.ChatRepoProvider
import com.p4indiafsm.features.chat.model.GroupUserDataModel
import com.p4indiafsm.features.chat.model.GroupUserResponseModel
import com.p4indiafsm.features.dashboard.presentation.DashboardActivity
import com.p4indiafsm.widgets.AppCustomEditText
import com.p4indiafsm.widgets.AppCustomTextView
import com.elvishew.xlog.XLog
import com.pnikosis.materialishprogress.ProgressWheel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.Serializable

class AddPeopleFragment : BaseFragment() {

    private lateinit var mContext: Context

    private lateinit var progress_wheel: ProgressWheel
    private lateinit var tv_no_data: AppCustomTextView
    private lateinit var rv_selected_list: RecyclerView
    private lateinit var rv_user_list: RecyclerView
    private lateinit var rl_add_group: RelativeLayout
    private lateinit var fab: FloatingActionButton

    private var grpUserAdapter: GroupUserListAdapter?= null
    private var selectedUserAdapter: SelectedUserListAdapter?= null
    private var groupUserList: ArrayList<GroupUserDataModel>?= null
    private var grpID = ""

    companion object {
        fun newInstance(grpID: Any): AddPeopleFragment {
            val fragment = AddPeopleFragment()

            val bundle = Bundle()
            bundle.putString("grpID", grpID as String?)
            fragment.arguments = bundle

            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context

        grpID = arguments?.getString("grpID").toString()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_add_people, container, false)

        initView(view)
        getChatUserListApi()

        (mContext as DashboardActivity).setSearchListener(object : SearchListener {
            override fun onSearchQueryListener(query: String) {
                if (query.isBlank()) {
                    grpUserAdapter?.refreshList(groupUserList!!)
                } else {
                    grpUserAdapter?.filter?.filter(query)
                }
            }
        })

        return view
    }

    private fun initView(view: View) {
        view.apply {
            progress_wheel = findViewById(R.id.progress_wheel)
            tv_no_data = findViewById(R.id.tv_no_data)
            rv_selected_list = findViewById(R.id.rv_selected_list)
            rv_user_list = findViewById(R.id.rv_user_list)
            rl_add_group = findViewById(R.id.rl_add_group)
            fab = findViewById(R.id.fab)
        }
        progress_wheel.stopSpinning()
        rv_user_list.layoutManager = LinearLayoutManager(mContext)
        rv_selected_list.layoutManager = LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false)
        selectedUserAdapter = SelectedUserListAdapter(mContext) { it, position ->
            for (i in groupUserList?.indices!!) {
                if (groupUserList?.get(i)?.id == it.id) {
                    groupUserList?.get(i)?.isSelected = false
                    grpUserAdapter?.refreshList(groupUserList!!)

                    selectedUserAdapter?.removeItem(position)

                    if (selectedUserAdapter?.selectedUserList?.isEmpty()!!)
                        fab.visibility = View.GONE
                    break
                }
            }
        }
        rv_selected_list.adapter = selectedUserAdapter

        rl_add_group.setOnClickListener(null)
        fab.setOnClickListener {
            when {
                selectedUserAdapter?.selectedUserList!!.isEmpty() -> (mContext as DashboardActivity).showSnackMessage(getString(R.string.error_select_user))
                else -> callAddMemberApi()
            }
        }
    }

    private fun getChatUserListApi() {
        if (!AppUtils.isOnline(mContext)) {
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
            return
        }

        progress_wheel.spin()
        val repository = ChatRepoProvider.provideChatRepository()
        BaseActivity.compositeDisposable.add(
                repository.memberUserList(grpID)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as GroupUserResponseModel
                            XLog.d("Get Group User List STATUS: " + response.status)
                            if (response.status == NetworkConstant.SUCCESS) {
                                progress_wheel.stopSpinning()
                                tv_no_data.visibility = View.GONE
                                groupUserList = response.group_user_list
                                setAdapter()
                            }
                            else {
                                progress_wheel.stopSpinning()
                                (mContext as DashboardActivity).showSnackMessage(response.message!!)
                            }

                        }, { error ->
                            error.printStackTrace()
                            progress_wheel.stopSpinning()
                            if (error != null)
                                XLog.d("Get Group User List ERROR: " + error.localizedMessage)
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                        })
        )
    }

    private fun callAddMemberApi() {
        if (!AppUtils.isOnline(mContext)) {
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
            return
        }

        var ids = ""

        selectedUserAdapter?.selectedUserList?.forEachIndexed { i, it ->
            if (i == selectedUserAdapter?.selectedUserList?.size!! - 1)
                ids += it.id
            else
                ids = ids + it.id + ","
        }


        progress_wheel.spin()
        val repository = ChatRepoProvider.provideChatRepository()
        BaseActivity.compositeDisposable.add(
                repository.addMember(grpID, ids)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as BaseResponse
                            XLog.d("Add Member STATUS: " + response.status)
                            progress_wheel.stopSpinning()
                            (mContext as DashboardActivity).showSnackMessage(response.message!!)

                            if (response.status == NetworkConstant.SUCCESS) {
                                (mContext as DashboardActivity).onBackPressed()
                            }

                        }, { error ->
                            error.printStackTrace()
                            progress_wheel.stopSpinning()
                            if (error != null)
                                XLog.d("Add Member ERROR: " + error.localizedMessage)
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                        })
        )
    }

    private fun setAdapter() {
        grpUserAdapter = GroupUserListAdapter(mContext, groupUserList) {
            for (i in groupUserList?.indices!!) {
                if (groupUserList?.get(i)?.id == it.id) {
                    if (groupUserList?.get(i)?.isSelected!!) {
                        groupUserList?.get(i)?.isSelected = false

                        for (j in selectedUserAdapter?.selectedUserList!!.indices) {
                            if (it.id == selectedUserAdapter?.selectedUserList?.get(j)?.id) {
                                selectedUserAdapter?.removeItem(j)

                                if (selectedUserAdapter?.selectedUserList?.isEmpty()!!)
                                    fab.visibility = View.GONE
                                break
                            }
                        }
                    }
                    else {
                        groupUserList?.get(i)?.isSelected = true
                        selectedUserAdapter?.addItem(groupUserList?.get(i)!!)
                        fab.visibility = View.VISIBLE
                    }
                    break
                }
            }

            /*if (groupUserList?.get(it)?.isSelected!!) {
                groupUserList?.get(it)?.isSelected = false

                for (i in selectedUserAdapter?.selectedUserList!!.indices) {
                    if (groupUserList?.get(it)?.id == selectedUserAdapter?.selectedUserList?.get(i)?.id) {
                        selectedUserAdapter?.removeItem(i)
                        break
                    }
                }
            }
            else {
                groupUserList?.get(it)?.isSelected = true
                selectedUserAdapter?.addItem(groupUserList?.get(it)!!)
            }*/
            grpUserAdapter?.refreshList(groupUserList!!)
        }
        rv_user_list.adapter = grpUserAdapter
    }
}