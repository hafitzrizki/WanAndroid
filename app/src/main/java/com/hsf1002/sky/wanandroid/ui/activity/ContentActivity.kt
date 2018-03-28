package com.hsf1002.sky.wanandroid.ui.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import com.hsf1002.sky.wanandroid.toast
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
        menuInflater.inflate(R.menu.menu_content, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId)
        {
            android.R.id.home ->
            {
                finish()
                return true
            }
            R.id.menuShare ->
            {
                Intent().run {
                    action = Intent.ACTION_SEND
                    putExtra(
                            Intent.EXTRA_TEXT, getString(R.string.share_article_url, getString(R.string.app_name), shareTitle, shareUrl)
                    )
                    type = Constant.CONTENT_SHARE_TYPE
                    startActivity(Intent.createChooser(this, "Share"))
                }
                return true
            }
            R.id.menuLike ->
            {
                if (!isLogin)
                {
                    Intent(this, LoginActivity::class.java).run {
                        startActivity(this)
                    }
                    toast(getString(R.string.login_please_login))
                    return true
                }

                // collect outside article
                if (shareID == 0)
                {
                    collectArticlePresenter.collectOutSideArticle(shareTitle, getString(R.string.outside_title), shareUrl, true)
                }
                else
                {
                    collectArticlePresenter.collectArticle(shareID, true)
                }
                return true
            }
            R.id.menuBrowser ->
            {
                Intent().run {
                    action = "android.intent.action.VIEW"
                    data = Uri.parse(shareUrl)
                    startActivity(this)
                }
                return true
            }
        }

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
        return  if (agentWeb.handleKeyEvent(keyCode, event))   true
            else {
                finish()
                super.onKeyDown(keyCode, event)
            }
    }

    override fun collectArticleSuccess(result: HomeListResponse, isAdd: Boolean) {
        toast(if (isAdd) getString(R.string.bookmark_success) else getString(R.string.bookmark_cancel_success))
    }

    override fun collectArticleFailed(errorMsg: String?, isAdd: Boolean) {
        toast(if (isAdd) getString(R.string.bookmark_failed, errorMsg) else getString(R.string.bookmark_cancel_failed, errorMsg))
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