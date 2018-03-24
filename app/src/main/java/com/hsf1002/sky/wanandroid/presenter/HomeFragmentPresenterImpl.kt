package com.hsf1002.sky.wanandroid.presenter

import com.hsf1002.sky.wanandroid.bean.BannerResponse
import com.hsf1002.sky.wanandroid.bean.HomeListResponse
import com.hsf1002.sky.wanandroid.view.CollectArticleView
import com.hsf1002.sky.wanandroid.view.HomeFragmentView

/**
 * Created by hefeng on 18-3-24.
 */

class HomeFragmentPresenterImpl(private val homeFragmentView: HomeFragmentView,
                                private val collectArticleView: CollectArticleView
):HomePresenter.OnHomeListListener, HomePresenter.OnCollectArticleListener, HomePresenter.OnBannerListener
{

    override fun getHomeList(page: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getHomeListSuccess(result: HomeListResponse) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getHomeListFailed(errorMsg: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun collectArticle(id: Int, isAdd: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun collectArticleSuccess(result: HomeListResponse, isAdd: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun collectArticleFailed(errorMsg: String, isAdd: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBanner() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBannerSuccess(result: BannerResponse) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBannerFailed(errorMsg: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}