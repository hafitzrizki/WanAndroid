package com.hsf1002.sky.wanandroid.model

import com.hsf1002.sky.wanandroid.presenter.SearchPresenter

/**
 * Created by hefeng on 18-3-28.
 */
interface SearchModel {
    fun getSearchList(onSearchListener:SearchPresenter.OnSearchListListener, page:Int = 0, k:String)
    fun cancelSearchRequest()

    fun getLikeList(onLikeListListener: SearchPresenter.OnLikeListListener, page:Int = 0)
    fun cancelLikeRequest()
}