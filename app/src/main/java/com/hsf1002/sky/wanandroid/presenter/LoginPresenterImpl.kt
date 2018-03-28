package com.hsf1002.sky.wanandroid.presenter

import com.hsf1002.sky.wanandroid.bean.LoginResponse
import com.hsf1002.sky.wanandroid.model.HomeModel
import com.hsf1002.sky.wanandroid.model.HomeModelImpl
import com.hsf1002.sky.wanandroid.view.LoginView

/**
 * Created by hefeng on 18-3-28.
 */
class LoginPresenterImpl (private var loginView:LoginView):
    HomePresenter.OnLoginListener, HomePresenter.OnRegisterListener
{
    private val homeModel:HomeModel = HomeModelImpl()

    override fun loginWanAndroid(username: String, password: String) {
        homeModel.loginWanAndroid(this, username, password)
    }

    override fun loginSuccess(result: LoginResponse) {
        if (result.errorCode != 0)
        {
            loginView.loginFailed(result.errorMsg)
        }
        else
        {
            loginView.loginSuccess(result)
            loginView.loginRegisterAfter(result)
        }
    }

    override fun loginFailed(errorMsg: String) {
        loginView.loginFailed(errorMsg)
    }

    override fun registerWanAndroid(username: String, password: String, repassword: String) {
        homeModel.registerWanAndroid(this, username, password, repassword)
    }

    override fun registerSuccess(result: LoginResponse) {
        if (result.errorCode != 0)
        {
            loginView.registerFailed(result.errorMsg)
        }
        else
        {
            loginView.registerSuccess(result)
            loginView.loginRegisterAfter(result)
        }
    }

    override fun registerFailed(errorMsg: String) {
        loginView.registerFailed(errorMsg)
    }

    fun cancelRequest()
    {
        homeModel.cancelLoginRequest()
        homeModel.cancelRegisterRequest()
    }
}