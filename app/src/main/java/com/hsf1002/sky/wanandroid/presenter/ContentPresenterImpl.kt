package com.hsf1002.sky.wanandroid.presenter

import com.hsf1002.sky.wanandroid.bean.HomeListResponse
import com.hsf1002.sky.wanandroid.model.CollectArticleModel
import com.hsf1002.sky.wanandroid.model.CollectOutsideArticleModel
import com.hsf1002.sky.wanandroid.model.CollectOutsideArticleModelImpl
import com.hsf1002.sky.wanandroid.model.SearchModelImpl
import com.hsf1002.sky.wanandroid.view.CollectArticleView

/**
 * Created by hefeng on 18-3-28.
 */
class ContentPresenterImpl(private val collectArticleView: CollectArticleView)
    :HomePresenter.OnCollectArticleListener, HomePresenter.OnCollectOutsideArticleListener
{
    private val collectArticleModel:CollectArticleModel = SearchModelImpl()
    private val collectOutsideArticleModel:CollectOutsideArticleModel = CollectOutsideArticleModelImpl()

    override fun collectArticle(id: Int, isAdd: Boolean) {
        collectArticleModel.collectArticle(this, id, isAdd)
    }

    override fun collectArticleSuccess(result: HomeListResponse, isAdd: Boolean) {
        if (result.errorCode != 0)
        {
            collectArticleView.collectArticleFailed(result.errorMsg, isAdd)
        }
        else
        {
            collectArticleView.collectArticleSuccess(result, isAdd)
        }
    }

    override fun collectArticleFailed(errorMsg: String, isAdd: Boolean) {
        collectArticleView.collectArticleFailed(errorMsg, isAdd)
    }

    override fun collectOutSideArticle(title: String, author: String, link: String, isAdd: Boolean) {
        collectOutsideArticleModel.collectOutsideArticle(this, title, author, link, isAdd)
    }

    override fun collectOutSideArticleSuccess(result: HomeListResponse, isAdd: Boolean) {
        if (result.errorCode != 0)
        {
            collectArticleView.collectArticleFailed(result.errorMsg, isAdd)
        }
        else
        {
            collectArticleView.collectArticleSuccess(result, isAdd)
        }
    }

    override fun collectOutSideArticleFailed(errorMsg: String, isAdd: Boolean) {
        collectArticleView.collectArticleFailed(errorMsg, isAdd)
    }

    fun cancelRequest()
    {
        collectArticleModel.cancelCollectRequest()
        collectOutsideArticleModel.cancelCollectOutsideRequest()
    }
}