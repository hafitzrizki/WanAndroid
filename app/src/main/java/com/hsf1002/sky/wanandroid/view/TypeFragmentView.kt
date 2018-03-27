package com.hsf1002.sky.wanandroid.view

import com.hsf1002.sky.wanandroid.bean.TreeListResponse

/**
 * Created by hefeng on 18-3-26.
 */
interface TypeFragmentView {
    fun getTypeListSuccess(result:TreeListResponse)
    fun getTypeListFailed(errorMsg:String?)
    fun getTypeListZero()
}