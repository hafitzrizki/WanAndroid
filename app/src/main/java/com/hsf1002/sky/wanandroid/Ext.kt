package com.hsf1002.sky.wanandroid

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.hsf1002.sky.wanandroid.constant.Constant

/**
 * Created by hefeng on 18-3-24.
 */


fun Context.toast(content: String)
{
    Constant.showToast?.apply {
        setText(content)
        show()
    }?:kotlin.run {
        Toast.makeText(this.applicationContext, content, Toast.LENGTH_SHORT).apply {
            Constant.showToast = this
        }.show()
    }
}


fun Context.inflater(resource:Int): View =
        LayoutInflater.from(this).inflate(resource, null)