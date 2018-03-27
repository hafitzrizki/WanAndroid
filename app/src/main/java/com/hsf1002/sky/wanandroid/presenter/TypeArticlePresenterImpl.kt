package com.hsf1002.sky.wanandroid.presenter

import com.hsf1002.sky.wanandroid.bean.ArticleListResponse
import com.hsf1002.sky.wanandroid.bean.HomeListResponse
import com.hsf1002.sky.wanandroid.model.CollectArticleModel
import com.hsf1002.sky.wanandroid.model.HomeModelImpl
import com.hsf1002.sky.wanandroid.model.TypeArticleModel
import com.hsf1002.sky.wanandroid.model.TypeArticleModelImpl
import com.hsf1002.sky.wanandroid.view.CollectArticleView
import com.hsf1002.sky.wanandroid.view.TypeArticleFragmentView

/**
 * Created by hefeng on 18-3-27.
 */
class TypeArticlePresenterImpl(private val typeArticleFragmentView: TypeArticleFragmentView,
                               private val collectArticleView: CollectArticleView)
    :TypeArticlePresenter, TypeArticlePresenter.OnTypeArticleListListener, HomePresenter.OnCollectArticleListener

{
    private val typeArticleModel:TypeArticleModel = TypeArticleModelImpl()
    private val collectArticleModel:CollectArticleModel = HomeModelImpl()


    override fun getTypeArticleList(page: Int, cid: Int) {
        typeArticleModel.getTypeArticleList(this, page, cid)
    }

    override fun getTypeArticleListSuccess(result: ArticleListResponse) {

        if (result.errorCode != 0)
        {
            typeArticleFragmentView.getTypeArticleListFailed(result.errorMsg)
            return
        }

        val total = result.data.total
        if (total == 0)
        {
            typeArticleFragmentView.getTypeArticleListZero()
            return
        }
        else if (total < result.data.size)
        {
            typeArticleFragmentView.getTypeArticleListSmall(result)
            return
        }
        typeArticleFragmentView.getTypeArticleListSuccess(result)
    }

    override fun getTypeArticleListFailed(errorMsg: String?) {
        typeArticleFragmentView.getTypeArticleListFailed(errorMsg)
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
        typeArticleModel.cancelRequest()
        collectArticleModel.cancelCollectRequest()
    }
}