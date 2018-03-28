package com.hsf1002.sky.wanandroid.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.gyf.barlibrary.ImmersionBar
import com.hsf1002.sky.wanandroid.R
import com.hsf1002.sky.wanandroid.base.BaseActivity
import com.hsf1002.sky.wanandroid.base.Preference
import com.hsf1002.sky.wanandroid.bean.LoginResponse
import com.hsf1002.sky.wanandroid.constant.Constant
import com.hsf1002.sky.wanandroid.presenter.LoginPresenterImpl
import com.hsf1002.sky.wanandroid.toast
import com.hsf1002.sky.wanandroid.view.LoginView
import kotlinx.android.synthetic.main.activity_login.*

/**
 * Created by hefeng on 18-3-24.
 */


class LoginActivity:BaseActivity(), LoginView
{
    private var isLogin:Boolean by Preference(Constant.LOGIN_KEY, false)
    private var user:String by Preference(Constant.USERNAME_KEY, "")
    private var pwd:String by Preference(Constant.PASSWORD_KEY, "")

    private val loginPresenter:LoginPresenterImpl by lazy {
        LoginPresenterImpl(this)
    }

    override fun setLayoutId(): Int = R.layout.activity_login

    override fun initImmersionBar() {
        super.initImmersionBar()
        if (ImmersionBar.isSupportStatusBarDarkFont())
        {
            immersionBar.statusBarDarkFont(true).init()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        login.setOnClickListener(this@LoginActivity.onClickListener)
        register.setOnClickListener(this@LoginActivity.onClickListener)
        loginExit.setOnClickListener(this@LoginActivity.onClickListener)
    }

    override fun cancelRequest() {
        loginPresenter.cancelRequest()
    }

    override fun loginSuccess(result: LoginResponse) {
        toast(getString(R.string.login_success))
    }

    override fun loginFailed(errorMsg: String?) {
        isLogin = false
        loginProgress.visibility = View.GONE
        errorMsg?.let {
            toast(it)
        }
    }

    override fun registerSuccess(result: LoginResponse) {
        toast(getString(R.string.register_success))
    }

    override fun registerFailed(errorMsg: String?) {
        isLogin = false
        loginProgress.visibility = View.GONE
        username.requestFocus()
        errorMsg?.let {
            toast(it)
        }
    }

    override fun loginRegisterAfter(result: LoginResponse) {
        isLogin = true
        user = result.data.username
        pwd = result.data.password

        loginProgress.visibility = View.GONE

        setResult(Activity.RESULT_OK, Intent().apply { putExtra(Constant.CONTENT_TITLE_KEY, result.data.username) })

        finish()
    }


    private val onClickListener = View.OnClickListener { view ->
        when (view.id)
        {
            R.id.login ->
            {
                if (checkContent())
                {
                    loginProgress.visibility = View.VISIBLE
                    loginPresenter.loginWanAndroid(username.text.toString(), password.text.toString())
                }
            }
            R.id.register ->
            {
                if (checkContent())
                {
                    loginProgress.visibility = View.VISIBLE
                    loginPresenter.registerWanAndroid(username.text.toString(), password.text.toString(), password.text.toString())
                }
            }
            R.id.loginExit ->
            {
                finish()
            }
        }
    }

    private fun checkContent():Boolean
    {
        username.error = null
        password.error = null

        var cancel = false
        var focusView:View? = null
        val usernameText = username.text.toString()
        val pwdText = password.text.toString()

        if (TextUtils.isEmpty(usernameText))
        {
            username.error = getString(R.string.username_not_empty)
            focusView = username
            cancel = true
        }

        if (TextUtils.isEmpty(pwdText))
        {
            password.error = getString(R.string.password_not_empty)
            focusView = password
            cancel = true
        }
        else if (pwdText.length < 6)
        {
            password.error = getString(R.string.password_length_short)
            focusView = password
            cancel = true
        }

        if (cancel)
        {
            focusView?.let {
                it.requestFocus()
            }
            return false
        }
        else
        {
            return true
        }
    }
}