package com.hsf1002.sky.wanandroid.presenter

import com.hsf1002.sky.wanandroid.bean.HomeListResponse
import com.hsf1002.sky.wanandroid.model.CollectArticleModel
import com.hsf1002.sky.wanandroid.view.CollectArticleView

/**
 * Created by hefeng on 18-3-28.
 */
class ContentPresenterImpl(private val collectArticleView: CollectArticleView)
    :HomePresenter.OnCollectArticleListener, HomePresenter.OnCollectOutsideArticleListener
{
    //private val collectArticleModel:CollectArticleModel =

    override fun collectArticle(id: Int, isAdd: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun collectArticleSuccess(result: HomeListResponse, isAdd: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun collectArticleFailed(errorMsg: String, isAdd: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun collectOutSideArticle(title: String, author: String, link: String, isAdd: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun collectOutSideArticleSuccess(result: HomeListResponse, isAdd: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun collectOutSideArticleFailed(errorMsg: String, isAdd: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}