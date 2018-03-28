package com.hsf1002.sky.wanandroid.view

import com.hsf1002.sky.wanandroid.bean.LoginResponse

/**
 * Created by hefeng on 18-3-28.
 */
interface LoginView {
    fun loginSuccess(result:LoginResponse)
    fun loginFailed(errorMsg:String?)
    fun registerSuccess(result:LoginResponse)
    fun registerFailed(errorMsg:String?)
    fun loginRegisterAfter(result:LoginResponse)
}