package com.hsf1002.sky.wanandroid.presenter

import com.hsf1002.sky.wanandroid.bean.HomeListResponse

/**
 * Created by hefeng on 18-3-28.
 */
interface SearchPresenter {
    interface OnSearchListListener
    {
        fun getSearchList(page:Int = 0, k:String)
        fun getSearchListSuccess(result:HomeListResponse)
        fun getSearchListFailed(errorMsg:String?)
    }

    interface OnLikeListListener
    {
        fun getLikeList(page:Int = 0)
        fun getLikeListSuccess(result:HomeListResponse)
        fun getLikeListFailed(errorMsg:String?)
    }
}