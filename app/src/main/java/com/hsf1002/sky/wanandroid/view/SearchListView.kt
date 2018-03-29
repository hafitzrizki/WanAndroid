package com.hsf1002.sky.wanandroid.view

import com.hsf1002.sky.wanandroid.bean.HomeListResponse

/**
 * Created by hefeng on 18-3-28.
 */
interface SearchListView {
    fun getSearchListSuccess(result:HomeListResponse)
    fun getSearchListFailed(errorMsg:String?)
    fun getSearchListZero()
    fun getSearchListSmall(result:HomeListResponse)

    fun getLikeListSuccess(result:HomeListResponse)
    fun getLikeListFailed(errorMsg:String?)
    fun getLikeListZero()
    fun getLikeListSmall(result:HomeListResponse)
}