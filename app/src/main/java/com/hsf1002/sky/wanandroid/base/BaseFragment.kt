package com.hsf1002.sky.wanandroid.base

import android.support.v4.app.Fragment

/**
 * Created by hefeng on 18-3-24.
 */

abstract class BaseFragment:Fragment()
{
    protected var isFirst:Boolean = true

    protected abstract fun cancelRequest()

    override fun onDestroyView() {
        super.onDestroyView()
        cancelRequest()
    }
}