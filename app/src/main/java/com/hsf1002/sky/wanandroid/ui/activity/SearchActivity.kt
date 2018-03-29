package com.hsf1002.sky.wanandroid.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import com.chad.library.adapter.base.BaseQuickAdapter
import com.hsf1002.sky.wanandroid.R
import com.hsf1002.sky.wanandroid.adapter.SearchAdapter
import com.hsf1002.sky.wanandroid.base.BaseActivity
import com.hsf1002.sky.wanandroid.base.Preference
import com.hsf1002.sky.wanandroid.bean.Datas
import com.hsf1002.sky.wanandroid.bean.HomeListResponse
import com.hsf1002.sky.wanandroid.constant.Constant
import com.hsf1002.sky.wanandroid.presenter.SearchPresenterImpl
import com.hsf1002.sky.wanandroid.toast
import com.hsf1002.sky.wanandroid.view.CollectArticleView
import com.hsf1002.sky.wanandroid.view.SearchListView
import kotlinx.android.synthetic.main.activity_search.*

/**
 * Created by hefeng on 18-3-24.
 */

class SearchActivity:BaseActivity(), SearchListView, CollectArticleView
{
    private val datas = mutableListOf<Datas>()

    private val searchPresenter: SearchPresenterImpl by lazy {
        SearchPresenterImpl(this, this)
    }

    private val searchAdapter:SearchAdapter by lazy {
        SearchAdapter(this, datas)
    }

    private var searchKey:String? = null
    private var searchView:SearchView? = null

    private var isSearch:Boolean = true
    private var isLike:Boolean = true
    private val isLogin:Boolean by Preference(Constant.LOGIN_KEY, false)


    override fun setLayoutId(): Int = R.layout.activity_search

    override fun initImmersionBar() {
        super.initImmersionBar()
        immersionBar.titleBar(R.id.search_toolbar).init()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.extras?.let {
            isSearch = it.getBoolean(Constant.SEARCH_KEY, true)
            isLike = it.getBoolean(Constant.LIKE_KEY, true)
        }

        search_toolbar.run {
            title = if (isSearch) ""
                    else if (isLike) getString(R.string.my_like)
                    else getString(R.string.my_bookmark)
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        swipeRefreshLayout.run {
            setOnRefreshListener(this@SearchActivity.onRefreshListener)
        }

        recyclerView.run {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = searchAdapter
        }

        searchAdapter.run {
            bindToRecyclerView(recyclerView)

            setOnLoadMoreListener({
                val page = searchAdapter.data.size /20 + 1
                if (!isSearch)
                    searchPresenter.getLikeList()
                else
                    searchKey?.let {
                        searchPresenter.getSearchList(page, it)
                    }

            }, recyclerView)

            onItemClickListener = this@SearchActivity.onItemClickListener
            onItemChildClickListener = this@SearchActivity.onItemChildClickListener
            setEmptyView(R.layout.fragment_home_empty)
        }

        if (!isSearch) {
            searchPresenter.getLikeList()
        }
        else
        {
            intent.extras?.let {
                searchKey = it.getString(Constant.CONTENT_TITLE_KEY, null)
            }
        }
    }

    override fun cancelRequest() {
        searchPresenter.cancelRequest()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isSearch)
        {
            menuInflater.inflate(R.menu.menu_search, menu)
            searchView = menu?.findItem(R.id.menuSearch)?.actionView as SearchView
            searchView?.init(1920, false, false, this@SearchActivity.onQueryTextListener)
            searchKey?.let {
                searchView?.setQuery(it, true)
            }
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home)
        {
            searchView?.clearFocus()
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun getSearchListSuccess(result: HomeListResponse) {
        result.data.datas?.let {
            searchAdapter.run {
                val total = result.data.total

                if (result.data.offset >= total || data.size >= total)
                {
                    loadMoreEnd()
                    return@let
                }

                if (swipeRefreshLayout.isRefreshing)
                {
                    replaceData(it)
                }
                else{
                    addData(it)
                }

                loadMoreComplete()
                setEnableLoadMore(true)
            }
        }
        swipeRefreshLayout.isRefreshing = false
    }

    override fun getSearchListFailed(errorMsg: String?) {
        searchAdapter.setEnableLoadMore(false)
        swipeRefreshLayout.isRefreshing = false
        searchAdapter.loadMoreFail()

        errorMsg?.let {
            toast(it)
        }?:let {
            toast(getString(R.string.get_data_error))
        }
        swipeRefreshLayout.isRefreshing = false
    }

    override fun getSearchListZero() {
        if (isSearch)
        {
            toast(getString(R.string.search_failed_not_article))
        }
        else
        {
            toast(getString(R.string.get_data_error))
        }

        swipeRefreshLayout.isRefreshing = false
    }

    override fun getSearchListSmall(result: HomeListResponse) {
        result.data.datas?.let {
            searchAdapter.run {
                replaceData(it)
                loadMoreEnd()
                loadMoreComplete()
                setEnableLoadMore(false)
            }
        }
        swipeRefreshLayout.isRefreshing = false
    }

    override fun collectArticleSuccess(result: HomeListResponse, isAdd: Boolean) {
        toast(
                if (isAdd)
                    getString(R.string.bookmark_success)
                else
                    getString(R.string.bookmark_cancel_success))
    }


    override fun collectArticleFailed(errorMsg: String?, isAdd: Boolean) {
        toast(
                if (isAdd)
                    getString(R.string.bookmark_failed)
                else
                    getString(R.string.bookmark_cancel_failed)
        )
    }

    override fun getLikeListSuccess(result: HomeListResponse) {
        getSearchListSuccess(result)
    }

    override fun getLikeListFailed(errorMsg: String?) {
        getSearchListFailed(errorMsg)
    }

    override fun getLikeListZero() {
        getSearchListZero()
    }

    override fun getLikeListSmall(result: HomeListResponse) {
        getSearchListSmall(result)
    }

    private val onQueryTextListener = object :SearchView.OnQueryTextListener{
        override fun onQueryTextSubmit(query: String?): Boolean {
            query?.let {
                searchKey = it
                swipeRefreshLayout.isRefreshing = true
                searchAdapter.setEnableLoadMore(false)
                searchPresenter.getSearchList(k = it)
            }?:let {
                swipeRefreshLayout.isRefreshing = false
                toast(getString(R.string.search_not_empty))
            }

            searchView?.clearFocus()
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean = false
    }

    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        if (!isSearch)
        {
            searchAdapter.setEnableLoadMore(false)
            swipeRefreshLayout.isRefreshing = true
            searchPresenter.getLikeList()
            return@OnRefreshListener
        }

        searchKey?.let {
            searchAdapter.setEnableLoadMore(false)
            swipeRefreshLayout.isRefreshing = true
            searchPresenter.getSearchList(k = it)
        }?:let {
            swipeRefreshLayout.isRefreshing = false
            toast(getString(R.string.search_not_empty))
        }
    }

    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
        if (datas.size != 0)
        {
            Intent(this, ContentActivity::class.java).run {
                putExtra(Constant.CONTENT_URL_KEY, datas[position].link)
                putExtra(Constant.CONTENT_TITLE_KEY, datas[position].title)
                startActivity(this)
            }
        }
    }

    private val onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { adapter, view, position ->
        if (datas.size != 0)
        {
            val data = datas[position]

            when (view.id)
            {
                R.id.homeItemType ->
                {
                    data.chapterName?:let {
                        toast(getString(R.string.type_null))
                        return@OnItemChildClickListener
                    }

                    Intent(this, TypeContentActivity::class.java).run {
                        putExtra(Constant.CONTENT_TARGET_KEY, true)
                        putExtra(Constant.CONTENT_TITLE_KEY, data.chapterName)
                        putExtra(Constant.CONTENT_ID_KEY, data.chapterId)
                        startActivity(this)
                    }
                }
                R.id.homeItemLike ->
                {
                    if (!isLogin)
                    {
                        Intent(this, LoginActivity::class.java).run {
                            startActivity(this)
                        }
                        toast(getString(R.string.login_please_login))
                        return@OnItemChildClickListener
                    }

                    // delete the like
                    if (!isSearch)
                    {
                        searchAdapter.remove(position)
                        searchPresenter.collectArticle(data.id, false)
                    }
                    else
                    {
                        val collect = data.collect
                        data.collect = !collect
                        searchAdapter.setData(position, data)
                        searchPresenter.collectArticle(data.id, !collect)
                    }
                }
            }
        }
    }

    // expand function
    private fun SearchView.init(sMaxWidth:Int = 0, sIconified:Boolean = false, isClose:Boolean = false, onQueryTextListener:SearchView.OnQueryTextListener)
     = kotlin.run {
        if (sMaxWidth != 0)
            maxWidth = sMaxWidth

        isIconified = sIconified

        if (!isClose)
            onActionViewExpanded()

        setOnQueryTextListener(onQueryTextListener)
    }
}