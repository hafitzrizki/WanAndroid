package com.hsf1002.sky.wanandroid.presenter

import com.hsf1002.sky.wanandroid.bean.TreeListResponse
import com.hsf1002.sky.wanandroid.model.HomeModel
import com.hsf1002.sky.wanandroid.model.HomeModelImpl
import com.hsf1002.sky.wanandroid.view.TypeFragmentView

/**
 * Created by hefeng on 18-3-26.
 */
class TypeFragmentPresenterImpl( private val typeFragmentView:TypeFragmentView ) :
    HomePresenter.OnTypeTreeListListener
{
    private val homeModel:HomeModel = HomeModelImpl()

    override fun getTypeTreeList() {
        homeModel.getTypeTreeList(this)
    }

    override fun getTypeTreeListSuccess(result: TreeListResponse) {
        if (result.errorCode != 0)
        {
            typeFragmentView.getTypeListFailed(result.errorMsg)
            return
        }

        if (result.data.isEmpty())
        {
            typeFragmentView.getTypeListZero()
            return
        }

        typeFragmentView.getTypeListSuccess(result)
    }

    override fun getTypeTreeListFailed(errorMsg: String?) {
        typeFragmentView.getTypeListFailed(errorMsg)
    }

    fun cancelRequest()
    {
        homeModel.cancelTypeTreeRequest()
    }
}