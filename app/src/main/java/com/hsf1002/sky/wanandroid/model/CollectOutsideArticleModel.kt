package com.hsf1002.sky.wanandroid.model

import com.hsf1002.sky.wanandroid.presenter.HomePresenter

/**
 * Created by hefeng on 18-3-28.
 */
interface CollectOutsideArticleModel {
    fun collectOutsideArticle(onCollectOutsideArticleListener: HomePresenter.OnCollectOutsideArticleListener,
                              title:String, author:String, link:String, isAdd:Boolean)
    fun cancelCollectOutsideRequest()
}