package com.hsf1002.sky.wanandroid.model

import com.hsf1002.sky.wanandroid.bean.ArticleListResponse
import com.hsf1002.sky.wanandroid.cancelByActive
import com.hsf1002.sky.wanandroid.constant.Constant
import com.hsf1002.sky.wanandroid.presenter.TypeArticlePresenter
import com.hsf1002.sky.wanandroid.retrofit.RetrofitHelper
import com.hsf1002.sky.wanandroid.tryCatch
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async

/**
 * Created by hefeng on 18-3-27.
 */
class TypeArticleModelImpl:TypeArticleModel {
    private var typeArticleListAsync:Deferred<ArticleListResponse> ? = null

    override fun getTypeArticleList(onTypeArticleListListener: TypeArticlePresenter.OnTypeArticleListListener, page: Int, cid: Int) {
        async(UI) {
            tryCatch ({
                it.printStackTrace()
                onTypeArticleListListener.getTypeArticleListFailed(it.toString())
            })
            {
                typeArticleListAsync?.cancelByActive()
                typeArticleListAsync = RetrofitHelper.retrofitService.getArticleList(page, cid)

                val result = typeArticleListAsync?.await()
                result?:let {
                    onTypeArticleListListener.getTypeArticleListFailed(Constant.RESULT_NULL)
                    return@async
                }
                onTypeArticleListListener.getTypeArticleListSuccess(result)
            }
        }
    }

    override fun cancelRequest() {
        typeArticleListAsync?.cancelByActive()
    }
}