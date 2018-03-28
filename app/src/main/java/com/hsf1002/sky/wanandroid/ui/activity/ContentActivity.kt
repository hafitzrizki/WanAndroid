package com.hsf1002.sky.wanandroid.ui.activity

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import com.hsf1002.sky.wanandroid.R
import com.hsf1002.sky.wanandroid.base.BaseActivity
import com.hsf1002.sky.wanandroid.base.Preference
import com.hsf1002.sky.wanandroid.bean.HomeListResponse
import com.hsf1002.sky.wanandroid.constant.Constant
import com.hsf1002.sky.wanandroid.getAgentWeb
import com.hsf1002.sky.wanandroid.presenter.ContentPresenterImpl
import com.hsf1002.sky.wanandroid.view.CollectArticleView
import com.just.agentweb.AgentWeb
import com.just.agentweb.ChromeClientCallbackManager
import kotlinx.android.synthetic.main.activity_content.*

/**
 * Created by hefeng on 18-3-24.
 */


class ContentActivity:BaseActivity(), CollectArticleView
{
    private lateinit var agentWeb:AgentWeb
    private lateinit var shareTitle:String
    private lateinit var shareUrl:String
    private var shareID:Int = 0

    private val collectArticlePresenter: ContentPresenterImpl by lazy {
        ContentPresenterImpl(this)
    }

    private val isLogin:Boolean by Preference(Constant.LOGIN_KEY, false)

    override fun setLayoutId(): Int = R.layout.activity_content

    override fun initImmersionBar() {
        super.initImmersionBar()
        immersionBar.titleBar(R.id.content_toolbar).init()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        content_toolbar.run {
            setTitle(getString(R.string.loading))
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        intent.extras?.let {
            shareID = it.getInt(Constant.CONTENT_ID_KEY, 0)
            shareUrl = it.getString(Constant.CONTENT_URL_KEY)
            shareTitle = it.getString(Constant.CONTENT_TITLE_KEY)

            agentWeb = shareUrl.getAgentWeb(this,
                    webContent,
                    LinearLayout.LayoutParams(-1, -1),
                    receivedTitleCallback)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        super.onPause()
        agentWeb.webLifeCycle.onPause()
    }

    override fun onResume() {
        super.onResume()
        agentWeb.webLifeCycle.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        agentWeb.webLifeCycle.onDestroy()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return super.onKeyDown(keyCode, event)
    }

    override fun collectArticleSuccess(result: HomeListResponse, isAdd: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun collectArticleFailed(errorMsg: String?, isAdd: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun cancelRequest() {
        collectArticlePresenter
    }

    private val receivedTitleCallback = ChromeClientCallbackManager.ReceivedTitleCallback { view, title ->
        title?.let {
            content_toolbar.title = it
        }
    }
}