package com.hsf1002.sky.wanandroid.presenter

import com.hsf1002.sky.wanandroid.bean.BannerResponse
import com.hsf1002.sky.wanandroid.bean.HomeListResponse
import com.hsf1002.sky.wanandroid.model.CollectArticleModel
import com.hsf1002.sky.wanandroid.model.HomeModel
import com.hsf1002.sky.wanandroid.model.HomeModelImpl
import com.hsf1002.sky.wanandroid.view.CollectArticleView
import com.hsf1002.sky.wanandroid.view.HomeFragmentView

/**
 * Created by hefeng on 18-3-24.
 */

class HomeFragmentPresenterImpl(private val homeFragmentView: HomeFragmentView,
                                private val collectArticleView: CollectArticleView
):HomePresenter.OnHomeListListener, HomePresenter.OnCollectArticleListener, HomePresenter.OnBannerListener
{

    private val homeModel:HomeModel = HomeModelImpl()
    private val collectArticleModel:CollectArticleModel = HomeModelImpl()

    override fun getHomeList(page: Int) {
        homeModel.getHomeList(this, page)
    }

    override fun getHomeListSuccess(result: HomeListResponse) {
        if (result.errorCode != 0) {
            homeFragmentView.getHomeListFailed(result.errorMsg)
            return
        }

        val total = result.data.total
        if (total == 0) {
            homeFragmentView.getHomeListZero()
            return
        } else if (total < result.data.size) {
            homeFragmentView.getHomeListSmall(result)
            return
        }
        else
        {
            homeFragmentView.getHomeListSuccess(result)
        }
    }

    override fun getHomeListFailed(errorMsg: String?) {
        homeFragmentView.getHomeListFailed(errorMsg)
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

    override fun getBanner() {
        homeModel.getBanner(this)
    }

    override fun getBannerSuccess(result: BannerResponse) {
        if (result.errorCode != 0)
        {
            homeFragmentView.getBannerFailed(result.errorMsg)
            return
        }

        result.data?:let {
            homeFragmentView.getBannerZero()
            return
        }

        homeFragmentView.getBannerSuccess(result)
    }

    override fun getBannerFailed(errorMsg: String) {
        homeFragmentView.getBannerFailed(errorMsg)
    }

    fun cancelRequest()
    {
        homeModel.cancelBannerRequest()
        homeModel.cancelHomeListRequest()
        collectArticleModel.cancelCollectRequest()
    }
}