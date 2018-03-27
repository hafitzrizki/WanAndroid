package com.hsf1002.sky.wanandroid.model

import com.hsf1002.sky.wanandroid.presenter.TypeArticlePresenter

/**
 * Created by hefeng on 18-3-27.
 */
interface TypeArticleModel {
    fun getTypeArticleList(onTypeArticleListListener: TypeArticlePresenter.OnTypeArticleListListener, page:Int = 0, cid:Int)
    fun cancelRequest()
}