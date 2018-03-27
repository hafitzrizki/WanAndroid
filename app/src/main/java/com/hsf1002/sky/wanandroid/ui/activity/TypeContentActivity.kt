package com.hsf1002.sky.wanandroid.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.Menu
import android.view.MenuItem
import com.hsf1002.sky.wanandroid.R
import com.hsf1002.sky.wanandroid.adapter.TypeArticlePagerAdapter
import com.hsf1002.sky.wanandroid.base.BaseActivity
import com.hsf1002.sky.wanandroid.bean.TreeListResponse
import com.hsf1002.sky.wanandroid.constant.Constant
import kotlinx.android.synthetic.main.activity_type_content.*

/**
 * Created by hefeng on 18-3-24.
 */

class TypeContentActivity:BaseActivity()
{
    private lateinit var firstTitle:String
    private val list = mutableListOf<TreeListResponse.Data.Children>()
    private var target:Boolean = false
    private val typeArticlePagerAdapter: TypeArticlePagerAdapter by lazy {
        TypeArticlePagerAdapter(list, supportFragmentManager)
    }

    override fun setLayoutId(): Int = R.layout.activity_type_content

    override fun cancelRequest() {

    }

    override fun initImmersionBar() {
        super.initImmersionBar()
        immersionBar.titleBar(R.id.typeSecondToolbar).init()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        typeSecondToolbar.run {
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        intent.extras?.let {
            extras ->
            extras.getString(Constant.CONTENT_TITLE_KEY)?.let {
                firstTitle = it
                typeSecondToolbar.title = it
            }

            target = extras.getBoolean(Constant.CONTENT_TARGET_KEY, false)

            if (target)
            {
                list.add(
                        TreeListResponse.Data.Children(extras.getInt(Constant.CONTENT_CID_KEY, 0),
                                firstTitle, 0, 0, 0, 0, null)
                )
            }
            else
            {
                extras.getSerializable(Constant.CONTENT_CHILDREN_DATA_KEY)?.let {
                    val data = it as TreeListResponse.Data
                    data.children?.let {
                        children ->
                        list.addAll(children)
                    }
                }
            }
        }

        typeSecondViewPager.run {
            adapter = typeArticlePagerAdapter
        }

        typeSecondTabs.run {
            setupWithViewPager(typeSecondViewPager)
        }

        typeSecondViewPager.addOnPageChangeListener(
                TabLayout.TabLayoutOnPageChangeListener(typeSecondTabs)
        )

        typeSecondTabs.addOnTabSelectedListener(
                TabLayout.ViewPagerOnTabSelectedListener(typeSecondViewPager)
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_type_content, menu)

        return true//super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId)
        {
            android.R.id.home ->
            {
                finish()
                return true
            }
            R.id.menuSearch ->
            {
                Intent(this, SearchActivity::class.java).run {
                    startActivity(this)
                }
                return true
            }
            R.id.menuShare ->
            {
                Intent().run {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT,
                            getString(R.string.share_type_url,
                                    getString(R.string.app_name),
                                    list[typeSecondTabs.selectedTabPosition].name,
                                    list[typeSecondTabs.selectedTabPosition].id))
                    //setType(Constant.CONTENT_SHARE_TYPE)
                    type = Constant.CONTENT_SHARE_TYPE
                    startActivity(Intent.createChooser(this, "Share"))
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}