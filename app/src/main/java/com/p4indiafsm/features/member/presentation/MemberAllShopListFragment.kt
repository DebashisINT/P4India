package com.p4indiafsm.features.member.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.p4indiafsm.CustomStatic
import com.p4indiafsm.MySingleton
import com.elvishew.xlog.XLog
import com.pnikosis.materialishprogress.ProgressWheel
import com.p4indiafsm.R
import com.p4indiafsm.app.AppDatabase
import com.p4indiafsm.app.NetworkConstant
import com.p4indiafsm.app.Pref
import com.p4indiafsm.app.SearchListener
import com.p4indiafsm.app.domain.MemberShopEntity
import com.p4indiafsm.app.types.FragType
import com.p4indiafsm.app.utils.AppUtils
import com.p4indiafsm.app.utils.Toaster
import com.p4indiafsm.base.presentation.BaseActivity
import com.p4indiafsm.base.presentation.BaseFragment
import com.p4indiafsm.features.addAttendence.api.addattendenceapi.AddAttendenceRepoProvider
import com.p4indiafsm.features.addAttendence.model.GetReportToFCMResponse
import com.p4indiafsm.features.addshop.presentation.AccuracyIssueDialog
import com.p4indiafsm.features.dashboard.presentation.DashboardActivity
import com.p4indiafsm.features.location.SingleShotLocationProvider
import com.p4indiafsm.features.member.api.TeamRepoProvider
import com.p4indiafsm.features.member.model.TeamShopListDataModel
import com.p4indiafsm.features.member.model.TeamShopListResponseModel
import com.p4indiafsm.features.nearbyshops.api.updateaddress.ShopAddressUpdateRepoProvider
import com.p4indiafsm.features.nearbyshops.model.updateaddress.AddressUpdateRequest
import com.p4indiafsm.features.nearbyshops.presentation.UpdateShopStatusDialog
import com.p4indiafsm.widgets.AppCustomTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONArray
import org.json.JSONObject
import java.util.HashMap

/**
 * Created by Saikat on 28-02-2020.
 */
//Revision History
// 1.0 MemberAllShopListFragment  AppV 4.0.6  Saheli    11/01/2023 IsAllowShopStatusUpdate
// 2.0 MemberAllShopListFragment  AppV 4.0.6  Saheli    13/01/2023 getTeamShopList function work for size 1
class MemberAllShopListFragment : BaseFragment() {

    private lateinit var mContext: Context

    private lateinit var rv_team_shop_list: RecyclerView
    private lateinit var tv_no_data_available: AppCustomTextView
    private lateinit var progress_wheel: ProgressWheel
    private lateinit var rl_member_list_shop_main: RelativeLayout
    private lateinit var tv_team_struct: AppCustomTextView
    private lateinit var tv_shop_count: AppCustomTextView
    private lateinit var tv_shop_path: AppCustomTextView

    private var userId = ""
    private var shopId = ""
    private var isBackPressed = false
    //private var shop_list: ArrayList<TeamShopListDataModel>? = null
    private var shop_list: ArrayList<TeamShopListDataModel> = ArrayList()
    private var adapter: MemberAllShopListAdapter? = null
    private var isAddressUpdating = false
    private var dialog: AccuracyIssueDialog? = null

    val shopIdList: MutableList<String> by lazy {
        listOf<String>().toMutableList()
    }

    val shopNameList: MutableList<String> by lazy {
        listOf<String>().toMutableList()
    }

    companion object {

        fun newInstance(userId: Any): MemberAllShopListFragment {
            val fragment = MemberAllShopListFragment()

            if (userId is String) {
                val bundle = Bundle()
                bundle.putString("user_id", userId)
                fragment.arguments = bundle
            }

            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context

        userId = arguments?.getString("user_id")?.toString()!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_team_shop_list, container, false)

        initView(view)

        CustomStatic.TeamUserSelect_user_id = userId

        isBackPressed = false
        getTeamShopList()

        (mContext as DashboardActivity).setSearchListener(object : SearchListener {
            override fun onSearchQueryListener(query: String) {
                if (query.isBlank()) {
                    adapter?.refreshList(shop_list!!)
                    //initAdapter(shop_list!!)
                    val shopType = AppDatabase.getDBInstance()?.shopTypeDao()?.getSingleType(shop_list?.get(0)?.shop_type!!)
                    if (shopType != null && !TextUtils.isEmpty(shopType.shoptype_name)) {
                        tv_shop_count.text = "Total " + shopType.shoptype_name + "(s): " + shop_list?.size
                    } else
                        tv_shop_count.text = "Total " + Pref.shopText + "(s): " + shop_list?.size
                } else {
                    adapter?.filter?.filter(query)
                }
            }
        })


        return view
    }

    private fun initView(view: View) {
        rv_team_shop_list = view.findViewById(R.id.rv_team_shop_list)
        rv_team_shop_list.layoutManager = LinearLayoutManager(mContext)

        tv_no_data_available = view.findViewById(R.id.tv_no_data_available)
        tv_team_struct = view.findViewById(R.id.tv_team_struct)
        tv_shop_count = view.findViewById(R.id.tv_shop_count)
        tv_shop_path = view.findViewById(R.id.tv_shop_path)

        progress_wheel = view.findViewById(R.id.progress_wheel)
        progress_wheel.stopSpinning()

        rl_member_list_shop_main = view.findViewById(R.id.rl_member_list_shop_main)
        rl_member_list_shop_main.setOnClickListener(null)

        tv_team_struct.apply {
            (mContext as DashboardActivity).teamHierarchy.takeIf { it.isNotEmpty() }?.let {
                visibility = View.VISIBLE

                it.forEachIndexed { i, name ->
                    text = if (i == 0)
                        name
                    else
                        text.toString().trim() + "-> " + name

                }

            } ?: let {
                visibility = View.GONE
            }
        }

        tv_shop_count.text = "Total " + Pref.shopText + "(s): 0"
        tv_no_data_available.text = "No " + Pref.shopText + " Available"
    }

    private fun getTeamShopList() {

        if (!AppUtils.isOnline(mContext)) {
            tv_no_data_available.visibility = View.VISIBLE
            (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
            return
        }

        progress_wheel.spin()
        val repository = TeamRepoProvider.teamRepoProvider()
        BaseActivity.compositeDisposable.add(
                repository.teamAllShopList(userId, shopId, (mContext as DashboardActivity).areaId)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result ->
                            val response = result as TeamShopListResponseModel
                            XLog.d("GET TEAM SHOP DATA : " + "RESPONSE : " + response.status + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name + ",MESSAGE : " + response.message)
                            progress_wheel.stopSpinning()
                            if (response.status == NetworkConstant.SUCCESS) {

                                response.team_struct?.let {
                                    tv_team_struct.text = it
                                }

                                if (response.shop_list != null && response.shop_list!!.size > 0) {
                                    //if(shopId.equals(""))
                                    response.shop_list = response.shop_list!!.distinctBy { it.shop_id } as ArrayList<TeamShopListDataModel>
                                    // 2.0 MemberAllShopListFragment  AppV 4.0.6 getTeamShopList function work for size 1
                                    try{
                                        if(response.shop_list!!.size>1){
                                            shop_list = response.shop_list!!.reversed() as ArrayList<TeamShopListDataModel>
                                            response.shop_list = response.shop_list!!.reversed() as ArrayList<TeamShopListDataModel>
                                        }
                                        shop_list = response.shop_list!!
                                    }catch (ex:Exception){
                                        ex.printStackTrace()
                                    }
                                    initAdapter(response.shop_list!!)
                                } else {
                                    if (TextUtils.isEmpty(shopId))
                                        tv_no_data_available.visibility = View.VISIBLE
                                    (mContext as DashboardActivity).showSnackMessage(response.message!!)
                                }
                            } else {
                                if (TextUtils.isEmpty(shopId))
                                    tv_no_data_available.visibility = View.VISIBLE
                                (mContext as DashboardActivity).showSnackMessage(response.message!!)
                            }

                        }, { error ->
                            progress_wheel.stopSpinning()
                            XLog.d("GET TEAM SHOP DATA : " + "ERROR : " + "\n" + "Time : " + AppUtils.getCurrentDateTime() + ", USER :" + Pref.user_name + ",MESSAGE : " + error.localizedMessage)
                            error.printStackTrace()
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                            if (TextUtils.isEmpty(shopId))
                                tv_no_data_available.visibility = View.VISIBLE
                        })
        )
    }

    @SuppressLint("SetTextI18n")
    private fun initAdapter(shop_list: ArrayList<TeamShopListDataModel>) {
        tv_no_data_available.visibility = View.GONE

        if (!isBackPressed && shopId.isNotEmpty())
            shopIdList.add(shopId)

        /*when {
            shop_list[0].shop_type == "1" -> tv_shop_count.text = "Total " + getString(R.string.shop_type) + "(s): " + shop_list.size
            shop_list[0].shop_type == "2" -> tv_shop_count.text = "Total " + getString(R.string.pp_type) + "(s): " + shop_list.size
            shop_list[0].shop_type == "3" -> tv_shop_count.text = "Total " + getString(R.string.new_party_type) + "(s): " + shop_list.size
            shop_list[0].shop_type == "4" -> tv_shop_count.text = "Total " + getString(R.string.distributor_type) + "(s): " + shop_list.size
            shop_list[0].shop_type == "5" -> tv_shop_count.text = "Total " + getString(R.string.diamond_type) + "(s): " + shop_list.size
        }*/

        val shopType = AppDatabase.getDBInstance()?.shopTypeDao()?.getSingleType(shop_list[0].shop_type)
        if (shopType != null && !TextUtils.isEmpty(shopType.shoptype_name)) {
            tv_shop_count.text = "Total " + shopType.shoptype_name + "(s): " + shop_list.size
        } else
            tv_shop_count.text = "Total " + Pref.shopText + "(s): " + shop_list.size

        if (!TextUtils.isEmpty(shopId)) {
            tv_shop_path.visibility = View.VISIBLE

            shopNameList.forEachIndexed { index, shopName ->
                if (index == shopNameList.size - 1)
                    tv_shop_path.text = shopName
                else
                    tv_shop_path.text = shopName + "-> "
            }
        } else
            tv_shop_path.visibility = View.GONE

        adapter = MemberAllShopListAdapter(mContext, shop_list, { teamShop: TeamShopListDataModel ->
            shopId = teamShop.shop_id
            shopNameList.add(teamShop.shop_name)
            isBackPressed = false
            getTeamShopList()
        }, { teamShop: TeamShopListDataModel ->

            if (AppUtils.mLocation != null) {
                if (AppUtils.mLocation!!.accuracy <= Pref.shopLocAccuracy.toFloat()) {
                    openAddressUpdateDialog(teamShop, AppUtils.mLocation!!)
                } else {
                    XLog.d("======Saved current location is inaccurate (Member Shop List)========")
                    getShopLatLong(teamShop)
                }
            } else {
                XLog.d("=====Saved current location is null (Member Shop List)======")
                getShopLatLong(teamShop)
            }

        }, { size: Int ->
            val shopType_ = AppDatabase.getDBInstance()?.shopTypeDao()?.getSingleType(shop_list[0].shop_type)
            if (shopType_ != null && !TextUtils.isEmpty(shopType_.shoptype_name)) {
                tv_shop_count.text = "Total " + shopType_.shoptype_name + "(s): " + size
            } else
                tv_shop_count.text = "Total " + Pref.shopText + "(s): " + size
        },
            { teamShop: TeamShopListDataModel ->
                if (!Pref.isAddAttendence)
                    (mContext as DashboardActivity).checkToShowAddAttendanceAlert()
                else if(Pref.IsAllowBreakageTrackingunderTeam) {
                    //CustomStatic.IsBreakageViewFromTeam = true
                    //(mContext as DashboardActivity).loadFragment(FragType.ShopDamageProductListFrag, true, teamShop.shop_id+"~"+userId)
                    teamShop.user_id = userId
                    (mContext as DashboardActivity).loadFragment(FragType.ShopDamageProductListFrag, true, teamShop)
                }

            },
        { teamShop: TeamShopListDataModel ->
            if (!Pref.isAddAttendence)
                (mContext as DashboardActivity).checkToShowAddAttendanceAlert()
            else if(Pref.IsNewQuotationfeatureOn) {
                (mContext as DashboardActivity).loadFragment(FragType.ViewAllQuotListFragment, true, teamShop)
        }

        },{
                if(Pref.IsFeedbackHistoryActivated){
                    if (!AppUtils.isOnline(mContext)) {
                        (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))
                    }
                    else{
                        (mContext as DashboardActivity).loadFragment(FragType.ShopFeedbackHisFrag, true, it)
                    }
                }
            }, { teamShop: TeamShopListDataModel ->   // 1.0 MemberAllShopListFragment  AppV 4.0.6  IsAllowShopStatusUpdate
                UpdateShopStatusDialog.getInstance(teamShop.shop_name!!, "Cancel", "Confirm", true,"","",
                    object : UpdateShopStatusDialog.OnDSButtonClickListener {
                        override fun onLeftClick() {

                        }
                        override fun onRightClick(status: String) {
                            if(!status.equals("")){
                                if(status.equals("Inactive")){
                                    var selShopId = teamShop.shop_id
                                    getFCMInfo(userId,selShopId,teamShop.shop_name)
                                }
                                if(status.equals("Active")){

                                }
                            }

                        }
                    }).show((mContext as DashboardActivity).supportFragmentManager, "")
            })

        rv_team_shop_list.adapter = adapter
    }

    private fun getShopLatLong(teamShop: TeamShopListDataModel) {
        if (!isAddressUpdating) {

            isAddressUpdating = true
            progress_wheel.spin()
            SingleShotLocationProvider.requestSingleUpdate(mContext,
                    object : SingleShotLocationProvider.LocationCallback {
                        override fun onStatusChanged(status: String) {
                        }

                        override fun onProviderEnabled(status: String) {
                        }

                        override fun onProviderDisabled(status: String) {
                        }

                        override fun onNewLocationAvailable(location: Location) {
                            progress_wheel.stopSpinning()
                            isAddressUpdating = false
                            if (location.accuracy > Pref.shopLocAccuracy.toFloat()) {
                                if (dialog == null) {
                                    dialog = AccuracyIssueDialog()
                                    dialog?.show((mContext as DashboardActivity).supportFragmentManager, "AccuracyIssueDialog")
                                } else {
                                    dialog?.dismissAllowingStateLoss()
                                    dialog?.show((mContext as DashboardActivity).supportFragmentManager, "AccuracyIssueDialog")

                                }
                            } else {
                                openAddressUpdateDialog(teamShop, location)
                            }
                        }
                    })
        }
    }

    private fun openAddressUpdateDialog(teamShop: TeamShopListDataModel, location: Location) {
        try {
            UpdateMemberShopAddressDialog.getInstance(teamShop, location, { mTeamShop: Any ->
                if (AppUtils.isOnline(mContext)) {
                    if (mTeamShop is TeamShopListDataModel)
                        callShopAddressUpdateApi(mTeamShop)
                } else
                    (mContext as DashboardActivity).showSnackMessage(getString(R.string.no_internet))

            }).show((mContext as DashboardActivity).supportFragmentManager, "UpdateShopAddressDialog")

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun callShopAddressUpdateApi(team: TeamShopListDataModel) {
        val repository = ShopAddressUpdateRepoProvider.provideShopAddressUpdateRepo()
        progress_wheel.spin()

        val addressUpdateReq = AddressUpdateRequest()
        addressUpdateReq.apply {
            user_id = Pref.user_id
            shop_id = team.shop_id
            shop_lat = team.shop_lat
            shop_long = team.shop_long
            shop_address = team.shop_address
            isAddressUpdated = "1"
            pincode = team.shop_pincode
        }

        BaseActivity.compositeDisposable.add(
                repository.getShopAddressUpdate(addressUpdateReq)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ response ->
                            progress_wheel.stopSpinning()
                            if (response.status == NetworkConstant.SUCCESS) {
                                (mContext as DashboardActivity).showSnackMessage(response.message!!)
                                getTeamShopList()
                            } else
                                (mContext as DashboardActivity).showSnackMessage(response.message!!)

                        }, { error ->
                            error.printStackTrace()
                            progress_wheel.stopSpinning()
                            (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                        })
        )

    }

    fun updateListOnBackPress() {
        isBackPressed = true

        shopIdList.also {
            it.removeAt(it.size - 1)
            shopId = if (it.size > 0)
                it[it.size - 1]
            else
                ""
        }

        shopNameList.also {
            if (it.size > 0)
                it[it.size - 1]
            else
                it[0]
        }

        getTeamShopList()
    }
    // 1.0 MemberAllShopListFragment  AppV 4.0.6  IsAllowShopStatusUpdate
    private fun getFCMInfo(usrID:String,shopID:String,shopName:String){
        try{
            val repository = AddAttendenceRepoProvider.addAttendenceRepo()
            BaseActivity.compositeDisposable.add(
                repository.getReportToFCMInfo(usrID,Pref.session_token.toString())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ result ->
                        val response = result as GetReportToFCMResponse

                        if (response.status == NetworkConstant.SUCCESS) {
                            sendFCMNotiForShopStatus(response.device_token!!,shopID,shopName)
                        }

                    }, { error ->
                        XLog.d("Apply Leave Response ERROR=========> " + error.message)
                        BaseActivity.isApiInitiated = false
                        progress_wheel.stopSpinning()
                        (mContext as DashboardActivity).showSnackMessage(getString(R.string.something_went_wrong))
                    })
            )
        }catch (ex:Exception){
            ex.printStackTrace()
        }

    }

    private fun sendFCMNotiForShopStatus(superVisor_fcmToken:String,shopID:String,shopName:String){
        if (superVisor_fcmToken != "") {
            try {
                val jsonObject = JSONObject()
                val notificationBody = JSONObject()
                notificationBody.put("body","Shop : $shopName inactive by ${Pref.user_name}")
                notificationBody.put("body1",shopID)
                //notificationBody.put("body2",shopName)
                //notificationBody.put("body3",Pref.user_name)
                notificationBody.put("flag", "shop_status_update")
                notificationBody.put("applied_user_id",Pref.user_id)
                jsonObject.put("data", notificationBody)
                val jsonArray = JSONArray()
                jsonArray.put(0,superVisor_fcmToken)
                jsonObject.put("registration_ids", jsonArray)
                sendCustomNotification(jsonObject)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    fun sendCustomNotification(notification: JSONObject) {
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest("https://fcm.googleapis.com/fcm/send", notification,
            object : Response.Listener<JSONObject?> {
                override fun onResponse(response: JSONObject?) {
                    (mContext as DashboardActivity).onBackPressed()
                }
            },
            object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError?) {

                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "key=AAAAIoWfCpc:APA91bEMOPyfjsyziPC1WYJiPHjzdmTQJmAOKP0fM24iXI9BgrmyhH4uLY6Jd-6Lpjp8mvSdpSp-zm20ApTOYQ3Ean4m6LicJ5CoECS_v5u2PUAwA8E6FLsu2ZC6_WxuSYnTTLzlUi4E"
                params["Content-Type"] = "application/json"
                return params
            }
        }

        MySingleton.getInstance(mContext)!!.addToRequestQueue(jsonObjectRequest)
    }
}