package com.hsf1002.sky.wanandroid.model

import com.hsf1002.sky.wanandroid.presenter.HomePresenter

/**
 * Created by hefeng on 18-3-26.
 */

interface HomeModel
{
    fun getHomeList(onHomeListListener: HomePresenter.OnHomeListListener, page:Int = 0)
    fun cancelHomeListRequest()

    fun getTypeTreeList(onTypeTreeListListener: HomePresenter.OnTypeTreeListListener)
    fun cancelTypeTreeList()

    fun loginWanAndroid(onLoginListener: HomePresenter.OnLoginListener, username:String, password:String)
    fun cancelLoginRequest()

    fun registerWanAndroid(onRegisterListener: HomePresenter.OnRegisterListener, username:String, password:String, repassword:String)
    fun cancelregisterRequest()

    fun getFriendList(onFriendListListener: HomePresenter.OnFriendListListener)
    fun cancelFriendRequest()

    fun getBanner(onBannerListener: HomePresenter.OnBannerListener)
    fun cancelBannerRequest()
}