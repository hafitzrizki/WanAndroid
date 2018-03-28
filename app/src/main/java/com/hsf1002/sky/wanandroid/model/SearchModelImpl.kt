package com.hsf1002.sky.wanandroid.model

import com.hsf1002.sky.wanandroid.bean.HomeListResponse
import com.hsf1002.sky.wanandroid.cancelByActive
import com.hsf1002.sky.wanandroid.constant.Constant
import com.hsf1002.sky.wanandroid.presenter.HomePresenter
import com.hsf1002.sky.wanandroid.presenter.SearchPresenter
import com.hsf1002.sky.wanandroid.retrofit.RetrofitHelper
import com.hsf1002.sky.wanandroid.tryCatch
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async

/**
 * Created by hefeng on 18-3-28.
 */
class SearchModelImpl : SearchModel, CollectArticleModel {

    private var searchListAsync:Deferred<HomeListResponse>? = null
    private var likeListAsync:Deferred<HomeListResponse>? = null
    private var collectArticleAsyn:Deferred<HomeListResponse>? = null

    override fun getSearchList(onSearchListener: SearchPresenter.OnSearchListListener, page: Int, k: String) {
        async(UI)
        {
            tryCatch ({
                it.printStackTrace()
                onSearchListener.getSearchListFailed(it.toString())
            })
            {
                searchListAsync.cancelByActive()
                searchListAsync = RetrofitHelper.retrofitService.getSearchList(page, k)

                val result = searchListAsync?.await()
                result?:let {
                    onSearchListener.getSearchListFailed(Constant.RESULT_NULL)
                    return@async
                }
                onSearchListener.getSearchListSuccess(result)
            }
        }
    }

    override fun cancelSearchRequest() {
        searchListAsync?.cancelByActive()
    }

    override fun collectArticle(onCollectArticleListener: HomePresenter.OnCollectArticleListener, id: Int, isAdd: Boolean) {
        async(UI)
        {
            tryCatch ({
                it.printStackTrace()
                onCollectArticleListener.collectArticleFailed(it.toString(), isAdd)
            })
            {
                collectArticleAsyn?.cancelByActive()
                collectArticleAsyn = if (isAdd)
                {
                    RetrofitHelper.retrofitService.addCollectArticle(id)
                }
                else{
                    RetrofitHelper.retrofitService.removeCollectArticle(id)
                }

                val result = collectArticleAsyn?.await()
                result?:let {
                    onCollectArticleListener.collectArticleFailed(Constant.RESULT_NULL, isAdd)
                    return@async
                }

                onCollectArticleListener.collectArticleSuccess(result, isAdd)
            }
        }
    }

    override fun cancelCollectRequest() {
        collectArticleAsyn?.cancelByActive()
    }

    override fun getLikeList(onLikeListListener: SearchPresenter.OnLikeListListener, page: Int) {
        async(UI) {
            tryCatch ({
                it.printStackTrace()
                onLikeListListener.getLikeListFailed(it.toString())
            })
            {
                likeListAsync?.cancelByActive()
                likeListAsync = RetrofitHelper.retrofitService.getLikeList(page)

                val result = likeListAsync?.await()
                result?:let {
                    onLikeListListener.getLikeListFailed(Constant.RESULT_NULL)
                    return@async
                }

                onLikeListListener.getLikeListSuccess(result)
            }
        }
    }

    override fun cancelLikeRequest() {
        likeListAsync?.cancelByActive()
    }
}