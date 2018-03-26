package com.hsf1002.sky.wanandroid.model

import com.hsf1002.sky.wanandroid.presenter.HomePresenter

/**
 * Created by hefeng on 18-3-26.
 */

interface CollectArticleModel
{
    fun collectArticle(onCollectArticleListener: HomePresenter.OnCollectArticleListener, id:Int, isAdd:Boolean)
    fun cancelCollectRequest()
}