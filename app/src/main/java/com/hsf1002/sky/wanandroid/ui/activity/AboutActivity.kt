package com.hsf1002.sky.wanandroid.ui.activity

import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.MenuItem
import com.hsf1002.sky.wanandroid.R
import com.hsf1002.sky.wanandroid.base.BaseActivity
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.content_about.*

/**
 * Created by hefeng on 18-3-24.
 */

class AboutActivity:BaseActivity()
{
    override fun setLayoutId(): Int = R.layout.activity_about

    override fun initImmersionBar() {
        super.initImmersionBar()
        immersionBar.titleBar(R.id.about_toolbar).init()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        about_toolbar.run {
            title = getString(R.string.my_about)
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        aboutVersion.text = getString(R.string.version_code, getString(R.string.app_name), packageManager.getPackageInfo(packageName, 0).versionName)

        @Suppress("DEPRECATION")
        aboutContent.run {
            text = Html.fromHtml(getString(R.string.about_content))
            movementMethod = LinkMovementMethod.getInstance()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home)
        {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun cancelRequest() {
    }
}