package com.hsf1002.sky.wanandroid.view

import com.hsf1002.sky.wanandroid.bean.BannerResponse
import com.hsf1002.sky.wanandroid.bean.HomeListResponse

/**
 * Created by hefeng on 18-3-24.
 */

interface HomeFragmentView
{
    fun getHomeListSuccess(result: HomeListResponse)
    fun getHomeListFailed(errorMsg: String?)
    fun getHomeListZero()
    fun getHomeListSmall(result:HomeListResponse)

    fun getBannerSuccess(result: BannerResponse)
    fun getBannerFailed(errorMsg:String?)
    fun getBannerZero()
}