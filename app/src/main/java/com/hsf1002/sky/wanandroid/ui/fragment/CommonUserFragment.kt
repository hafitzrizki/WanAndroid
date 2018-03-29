package com.hsf1002.sky.wanandroid.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.hsf1002.sky.wanandroid.R
import com.hsf1002.sky.wanandroid.adapter.CommonAdapter
import com.hsf1002.sky.wanandroid.adapter.CommonUseTagAdapter
import com.hsf1002.sky.wanandroid.adapter.HotTagAdapter
import com.hsf1002.sky.wanandroid.base.BaseFragment
import com.hsf1002.sky.wanandroid.bean.FriendListResponse
import com.hsf1002.sky.wanandroid.bean.HotKeyResponse
import com.hsf1002.sky.wanandroid.constant.Constant
import com.hsf1002.sky.wanandroid.inflater
import com.hsf1002.sky.wanandroid.presenter.CommonUseFragmentPresenterImpl
import com.hsf1002.sky.wanandroid.toast
import com.hsf1002.sky.wanandroid.ui.activity.ContentActivity
import com.hsf1002.sky.wanandroid.ui.activity.SearchActivity
import com.hsf1002.sky.wanandroid.view.CommonUseFragmentView
import com.zhy.view.flowlayout.TagFlowLayout
import kotlinx.android.synthetic.main.fragment_common.*

/**
 * Created by hefeng on 18-3-24.
 */

class CommonUserFragment:BaseFragment(), CommonUseFragmentView {
    private var mainView: View? = null
    private val datas = mutableListOf<FriendListResponse.Data>()
    private val commonAdapter: CommonAdapter by lazy {
        CommonAdapter(activity, datas)
    }

    private val commonUseFragmentPresenter: CommonUseFragmentPresenterImpl by lazy {
        CommonUseFragmentPresenterImpl(this)
    }

    private lateinit var flowLayout: LinearLayout   // contains my bookmark, everyone search and common use website

    // everyone search
    private lateinit var hotTagFlowLayout: TagFlowLayout
    private val hotTagDatas = mutableListOf<HotKeyResponse.Data>()
    private val hotTagAdapter: HotTagAdapter by lazy {
        HotTagAdapter(activity, hotTagDatas)
    }

    // common use website
    private lateinit var commonUseTagFlowLayout: TagFlowLayout
    private val commonUseDatas = mutableListOf<FriendListResponse.Data>()
    private val commonUseTagAdapter: CommonUseTagAdapter by lazy {
        CommonUseTagAdapter(activity, commonUseDatas)
    }

    // my bookmark
    private lateinit var bookmarkTitle: TextView
    private lateinit var bookmarkTagFlowLayout: TagFlowLayout
    private val bookmarkUseDatas = mutableListOf<FriendListResponse.Data>()
    private val bookmarkTagAdapter: CommonUseTagAdapter by lazy {
        CommonUseTagAdapter(activity, bookmarkUseDatas)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mainView?:let {
            mainView = inflater?.inflate(R.layout.fragment_common, container, false)

            flowLayout = activity.inflater(R.layout.common_hot) as LinearLayout
            // my bookmark
            bookmarkTagFlowLayout = flowLayout.findViewById<TagFlowLayout>(R.id.bookmarkFlowLayout)
            bookmarkTitle = flowLayout.findViewById<TextView>(R.id.bookmarkTitle)
            // everyone search
            hotTagFlowLayout = flowLayout.findViewById<TagFlowLayout>(R.id.hotFlowLayout)
            // common use website
            commonUseTagFlowLayout = flowLayout.findViewById<TagFlowLayout>(R.id.commonUseFlowLayout)
        }
        return mainView// super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        bookmarkTagFlowLayout.run {
            adapter = bookmarkTagAdapter
            setOnTagClickListener(this@CommonUserFragment.onBookmarkTagClickListener)
        }

        hotTagFlowLayout.run {
            adapter = hotTagAdapter
            setOnTagClickListener(this@CommonUserFragment.onHotTagClickListener)
        }

        commonUseTagFlowLayout.run {
            adapter = commonUseTagAdapter
            setOnTagClickListener(this@CommonUserFragment.onCommonUseTagClickListener)
        }


        commonSwipeRefreshLayout.run {
            isRefreshing = true
            setOnRefreshListener(this@CommonUserFragment.onRefreshListener)
        }

        commonRecyclerView.run {
            layoutManager = LinearLayoutManager(activity)
            adapter = commonAdapter
        }

        commonAdapter.run {
            bindToRecyclerView(commonRecyclerView)
            addHeaderView(flowLayout)       //////////////////// bind flowlayout to recyclerview////////////////////
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        if (!hidden && isFirst)
        {
            commonUseFragmentPresenter.getFriendList()
            isFirst = false
        }
    }

    override fun cancelRequest() {
        commonUseFragmentPresenter.cancelRequest()
    }

    override fun getFriendListSuccess(bookmarkResult: FriendListResponse?, commonResult: FriendListResponse?, hotResult: HotKeyResponse) {
        // my bookmark
        bookmarkResult?.let {
            it.data?.let {

                bookmarkTitle.visibility = View.VISIBLE
                bookmarkTagFlowLayout.visibility = View.VISIBLE

                bookmarkUseDatas.clear()
                bookmarkUseDatas.addAll(it)

                bookmarkTagAdapter.notifyDataChanged()
            }?:let {            // maybe empty
                bookmarkTitle.visibility = View.GONE
                bookmarkTagFlowLayout.visibility = View.GONE
            }
        }

        // common use website
        commonResult?.data?.let {
            commonUseDatas.clear()
            commonUseDatas.addAll(it)

            commonUseTagAdapter.notifyDataChanged()
        }

        // everyone search
        hotResult.data?.let {
            hotTagDatas.clear()
            hotTagDatas.addAll(it)

            hotTagAdapter.notifyDataChanged()
        }

        commonSwipeRefreshLayout.isRefreshing = false
    }

    override fun getFriendListFailed(errorMsg: String?) {
        errorMsg?.let {
            activity.toast(it)
        }?:let {
            activity.toast(getString(R.string.get_data_error))
        }
        commonSwipeRefreshLayout.isRefreshing = false
    }

    override fun getFriendListZero() {
        activity.toast(getString(R.string.get_data_zero))
        commonSwipeRefreshLayout.isRefreshing = false
    }

    fun refreshData()
    {
        commonSwipeRefreshLayout.isRefreshing = true
        commonUseFragmentPresenter.getFriendList()
    }

    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        refreshData()
    }

    // my bookmark
    private val onBookmarkTagClickListener = TagFlowLayout.OnTagClickListener { view, position, parent ->
        if (bookmarkUseDatas.size != 0)
        {
            Intent(activity, ContentActivity::class.java).run {
                putExtra(Constant.CONTENT_URL_KEY, bookmarkUseDatas[position].link)
                putExtra(Constant.CONTENT_ID_KEY, bookmarkUseDatas[position].id)
                putExtra(Constant.CONTENT_TITLE_KEY, bookmarkUseDatas[position].name)
                startActivity(this)
            }
        }
        true
    }

    // common use website
    private val onCommonUseTagClickListener = TagFlowLayout.OnTagClickListener { view, position, parent ->

        if (commonUseDatas.size != 0)
        {
            Intent(activity, ContentActivity::class.java).run {
                putExtra(Constant.CONTENT_URL_KEY, commonUseDatas[position].link)
                putExtra(Constant.CONTENT_ID_KEY, commonUseDatas[position].id)
                putExtra(Constant.CONTENT_TITLE_KEY, commonUseDatas[position].name)
                startActivity(this)
            }
        }
        true
    }

    // everyone search
    private val onHotTagClickListener = TagFlowLayout.OnTagClickListener { view, position, parent ->
        if (hotTagDatas.size != 0)
        {
            Intent(activity, SearchActivity::class.java).run {
                putExtra(Constant.SEARCH_KEY, true)
                putExtra(Constant.CONTENT_TITLE_KEY, hotTagDatas[position].name)
                startActivityForResult(this, Constant.MAIN_LIKE_REQUEST_CODE)
            }
        }
        true
    }
}