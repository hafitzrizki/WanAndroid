package com.hsf1002.sky.wanandroid.presenter

import com.hsf1002.sky.wanandroid.bean.FriendListResponse
import com.hsf1002.sky.wanandroid.bean.HotKeyResponse
import com.hsf1002.sky.wanandroid.model.HomeModel
import com.hsf1002.sky.wanandroid.model.HomeModelImpl
import com.hsf1002.sky.wanandroid.view.CommonUseFragmentView

/**
 * Created by hefeng on 18-3-29.
 */
class CommonUseFragmentPresenterImpl (private val commonUseFragmentView: CommonUseFragmentView)
    :HomePresenter.OnFriendListListener
{
    private val homeModel: HomeModel = HomeModelImpl()

    override fun getFriendList() {
        homeModel.getFriendList(this)
    }

    override fun getFriendListSuccess(bookmarkResult: FriendListResponse?, commonResult: FriendListResponse, hotResult: HotKeyResponse) {
        if (commonResult.errorCode != 0 || hotResult.errorCode != 0)
        {
            commonUseFragmentView.getFriendListFailed(commonResult.errorMsg)
            return
        }

        if (commonResult.data == null || hotResult.data == null)
        {
            commonUseFragmentView.getFriendListZero()
            return
        }
        /*commonResult.data?.apply {  }?.apply {
            commonUseFragmentView.getFriendListZero()
            return
        }*/

        if (commonResult.data?.size == 0 && hotResult.data?.size == 0)
        {
            commonUseFragmentView.getFriendListZero()
            return
        }

        commonUseFragmentView.getFriendListSuccess(bookmarkResult, commonResult, hotResult)
    }

    override fun getFriendListFailed(errorMsg: String) {
        commonUseFragmentView.getFriendListFailed(errorMsg)
    }

    fun cancelRequest()
    {
        homeModel.cancelFriendRequest()
    }
}