package com.hsf1002.sky.wanandroid.model

import com.hsf1002.sky.wanandroid.bean.HomeListResponse
import com.hsf1002.sky.wanandroid.cancelByActive
import com.hsf1002.sky.wanandroid.constant.Constant
import com.hsf1002.sky.wanandroid.presenter.HomePresenter
import com.hsf1002.sky.wanandroid.retrofit.RetrofitHelper
import com.hsf1002.sky.wanandroid.tryCatch
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async

/**
 * Created by hefeng on 18-3-28.
 */
class CollectOutsideArticleModelImpl:CollectOutsideArticleModel {

    private var collectOutsideArticleAsync:Deferred<HomeListResponse>? = null

    override fun collectOutsideArticle(onCollectOutsideArticleListener: HomePresenter.OnCollectOutsideArticleListener, title: String, author: String, link: String, isAdd: Boolean) {
        async(UI)
        {
            tryCatch ({
                it.printStackTrace()
                onCollectOutsideArticleListener.collectOutSideArticleFailed(it.toString(), isAdd)
            })
            {
                collectOutsideArticleAsync?.cancelByActive()
                collectOutsideArticleAsync = RetrofitHelper.retrofitService.addCollectOutsideArticle(title, author, link)

                val result = collectOutsideArticleAsync?.await()
                result?:let {
                    onCollectOutsideArticleListener.collectOutSideArticleFailed(Constant.RESULT_NULL, isAdd)
                    return@async
                }
                onCollectOutsideArticleListener.collectOutSideArticleSuccess(result, isAdd)
            }
        }
    }

    override fun cancelCollectOutsideRequest() {
        collectOutsideArticleAsync?.cancelByActive()
    }
}