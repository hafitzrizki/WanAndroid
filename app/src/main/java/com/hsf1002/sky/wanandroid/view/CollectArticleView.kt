package com.hsf1002.sky.wanandroid.view

import com.hsf1002.sky.wanandroid.bean.HomeListResponse

/**
 * Created by hefeng on 18-3-24.
 */
interface CollectArticleView {
    fun collectArticleSuccess(result:HomeListResponse, isAdd:Boolean)
    fun collectArticleFailed(errorMsg:String?, isAdd:Boolean)
}