package com.hsf1002.sky.wanandroid.view

import com.hsf1002.sky.wanandroid.bean.ArticleListResponse

/**
 * Created by hefeng on 18-3-27.
 */
interface TypeArticleFragmentView {
    fun getTypeArticleListSuccess(result:ArticleListResponse)
    fun getTypeArticleListFailed(errorMsg:String?)
    fun getTypeArticleListZero()
    fun getTypeArticleListSmall(result:ArticleListResponse)
}