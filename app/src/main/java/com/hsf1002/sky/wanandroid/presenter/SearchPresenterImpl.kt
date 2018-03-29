package com.hsf1002.sky.wanandroid.presenter

import com.hsf1002.sky.wanandroid.bean.HomeListResponse
import com.hsf1002.sky.wanandroid.model.*
import com.hsf1002.sky.wanandroid.view.CollectArticleView
import com.hsf1002.sky.wanandroid.view.SearchListView

/**
 * Created by hefeng on 18-3-28.
 */
class SearchPresenterImpl(private val searchView:SearchListView, private val collectArticleView: CollectArticleView):
    SearchPresenter.OnSearchListListener, SearchPresenter.OnLikeListListener, HomePresenter.OnCollectArticleListener
{
    private val searchModel:SearchModel = SearchModelImpl()
    private val collectArticleModel: CollectArticleModel = SearchModelImpl()


    override fun getSearchList(page: Int, k: String) {
        searchModel.getSearchList(this, page, k)
    }

    override fun getSearchListSuccess(result: HomeListResponse) {
        if (result.errorCode != 0)
        {
            searchView.getSearchListFailed(result.errorMsg)
            return
        }

        val total = result.data.total
        if (total == 0)
        {
            searchView.getSearchListZero()
            return
        }
        if (total < result.data.size)
        {
            searchView.getSearchListSmall(result)
            return
        }
        searchView.getSearchListSuccess(result)
    }

    override fun getSearchListFailed(errorMsg: String?) {
        searchView.getSearchListFailed(errorMsg)
    }

    override fun getLikeList(page: Int) {
        searchModel.getLikeList(this, page)
    }

    override fun getLikeListSuccess(result: HomeListResponse) {
        if (result.errorCode != 0)
        {
            searchView.getLikeListFailed(result.errorMsg)
            return
        }

        val total = result.data.total
        if (total == 0)
        {
            searchView.getLikeListZero()
            return
        }
        if (total < result.data.size)
        {
            searchView.getLikeListSmall(result)
            return
        }
        searchView.getLikeListSuccess(result)
    }

    override fun getLikeListFailed(errorMsg: String?) {
        searchView.getLikeListFailed(errorMsg)
    }

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

    fun cancelRequest()
    {
        searchModel.cancelSearchRequest()
        searchModel.cancelLikeRequest()
        collectArticleModel.cancelCollectRequest()
    }
}