package com.hsf1002.sky.wanandroid.presenter

import com.hsf1002.sky.wanandroid.bean.ArticleListResponse

/**
 * Created by hefeng on 18-3-27.
 */
interface TypeArticlePresenter {
    fun getTypeArticleList(page:Int = 0, cid:Int)

    interface OnTypeArticleListListener {
        fun getTypeArticleListSuccess(result:ArticleListResponse)
        fun getTypeArticleListFailed(errorMsg:String?)
    }
}