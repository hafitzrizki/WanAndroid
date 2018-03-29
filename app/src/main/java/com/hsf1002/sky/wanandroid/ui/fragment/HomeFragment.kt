package com.hsf1002.sky.wanandroid.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.hsf1002.sky.wanandroid.R
import com.hsf1002.sky.wanandroid.adapter.BannerAdapter
import com.hsf1002.sky.wanandroid.adapter.HomeAdapter
import com.hsf1002.sky.wanandroid.base.BaseFragment
import com.hsf1002.sky.wanandroid.base.Preference
import com.hsf1002.sky.wanandroid.bean.BannerResponse
import com.hsf1002.sky.wanandroid.bean.Datas
import com.hsf1002.sky.wanandroid.bean.HomeListResponse
import com.hsf1002.sky.wanandroid.constant.Constant
import com.hsf1002.sky.wanandroid.inflater
import com.hsf1002.sky.wanandroid.presenter.HomeFragmentPresenterImpl
import com.hsf1002.sky.wanandroid.toast
import com.hsf1002.sky.wanandroid.ui.activity.ContentActivity
import com.hsf1002.sky.wanandroid.ui.activity.LoginActivity
import com.hsf1002.sky.wanandroid.ui.activity.TypeContentActivity
import com.hsf1002.sky.wanandroid.view.CollectArticleView
import com.hsf1002.sky.wanandroid.view.HomeFragmentView
import com.hsf1002.sky.wanandroid.view.HorizontalRecyclerView
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch

/**
 * Created by hefeng on 18-3-24.
 */

class HomeFragment:BaseFragment(), HomeFragmentView, CollectArticleView
{
    companion object {
        private const val BANNER_TIME = 5000L
    }


    private var mainView: View? = null
    private val datas = mutableListOf<Datas>()

    private val homeFragmentPresenter:HomeFragmentPresenterImpl by lazy {
        HomeFragmentPresenterImpl(this, this)
    }

    private val homeAdapter:HomeAdapter by lazy {
        HomeAdapter(activity, datas)
    }

    private val isLogin:Boolean by Preference(Constant.LOGIN_KEY, false)
    private lateinit var bannerRecyclerView:HorizontalRecyclerView
    private val bannerDatas = mutableListOf<BannerResponse.Data>()
    private val bannerAdapter:BannerAdapter by lazy {
        BannerAdapter(activity, bannerDatas)
    }

    /* make banner recycler view perform like viewpager, but just one item once */
    private val bannerPagerSnap: PagerSnapHelper by lazy {
        PagerSnapHelper()
    }

    private val linearLayoutManager:LinearLayoutManager by lazy {
        LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
    }

    private var bannerSwitchJob: Job? = null

    private var currentIndex = 0


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mainView?:let {
            mainView = inflater?.inflate(R.layout.fragment_home, container, false)
            bannerRecyclerView = activity.inflater(R.layout.home_banner) as HorizontalRecyclerView
        }
        return mainView// super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        swipeRefreshLayout.run {
            isRefreshing = true
            setOnRefreshListener(onRefreshListener)
        }

        recyclerView.run {
            layoutManager = LinearLayoutManager(activity)
            adapter = homeAdapter
        }

        // PagerSnapHelper的展示效果和LineSnapHelper是一样的，只是PagerSnapHelper 限制一次只能滑动一页，不能快速滑动
        bannerRecyclerView.run {
            layoutManager = linearLayoutManager
            bannerPagerSnap.attachToRecyclerView(this)
            requestDisallowInterceptTouchEvent(true)
            setOnTouchListener(onTouchListener)
            addOnScrollListener(onScrollListener)
        }

        bannerAdapter.run {
            bindToRecyclerView(bannerRecyclerView)
            setOnItemClickListener(onBannerItemClickListener)
        }

        homeAdapter.run {
            bindToRecyclerView(recyclerView)
            setOnLoadMoreListener(onRequestLoadMoreListener, recyclerView)
            setOnItemClickListener(this@HomeFragment.onItemClickListener)
            setOnItemChildClickListener(this@HomeFragment.onItemChildClickListener)
            addHeaderView(bannerRecyclerView)
            setEmptyView(R.layout.fragment_home_empty)
        }

        homeFragmentPresenter.getBanner()
        homeFragmentPresenter.getHomeList()
    }

    override fun onPause() {
        super.onPause()
        cancelSwtichJob()
    }

    override fun onResume() {
        super.onResume()
        startSwitchJob()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        if (hidden)
        {
            cancelSwtichJob()
        }
        else
        {
            startSwitchJob()
        }
    }

    override fun cancelRequest() {
        homeFragmentPresenter.cancelRequest()
    }

    override fun collectArticleSuccess(result: HomeListResponse, isAdd: Boolean) {
        activity.toast(
                if (isAdd)
                {
                    getString(R.string.bookmark_success)
                }
                else
                {
                    getString(R.string.bookmark_cancel_success)
                }
        )
    }

    override fun collectArticleFailed(errorMsg: String?, isAdd: Boolean) {
        activity.toast(
                if (isAdd)
                {
                    getString(R.string.bookmark_failed)
                }
                else
                {
                    getString(R.string.bookmark_cancel_failed)
                }
        )
    }

    override fun getHomeListSuccess(result: HomeListResponse) {
        result.data.datas?.let {
            homeAdapter.run {
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
                else
                {
                    addData(it)
                }
                loadMoreComplete()
                setEnableLoadMore(true)
            }
        }
        swipeRefreshLayout.isRefreshing = false
    }

    override fun getHomeListFailed(errorMsg: String?) {
        homeAdapter.setEnableLoadMore(false)
        homeAdapter.loadMoreFail()
        errorMsg?.let {
            activity.toast(it)
        }?:let {
            activity.toast(getString(R.string.get_data_error))
        }
        swipeRefreshLayout.isRefreshing = false
    }

    override fun getHomeListZero() {
        activity.toast(getString(R.string.get_data_zero))
        swipeRefreshLayout.isRefreshing =false
    }

    override fun getHomeListSmall(result: HomeListResponse) {
        result.data.datas?.let {
            homeAdapter.run {
                replaceData(it)
                loadMoreComplete()
                loadMoreEnd()
                setEnableLoadMore(false)
            }
        }
        swipeRefreshLayout.isRefreshing = false
    }

    override fun getBannerSuccess(result: BannerResponse) {
        result.data?.let {
            bannerAdapter.replaceData(it)
            startSwitchJob()
        }
    }

    override fun getBannerFailed(errorMsg: String?) {
        errorMsg?.let {
            activity.toast(it)
        }?:let {
            activity.toast(getString(R.string.get_data_error))
        }
    }

    override fun getBannerZero() {
        activity.toast(getString(R.string.get_data_zero))
    }

    fun smoothScrollToPosition() = recyclerView.scrollToPosition(0)

    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        refreshData()
    }

    fun refreshData()
    {
        swipeRefreshLayout.isRefreshing = true
        homeAdapter.setEnableLoadMore(false)
        cancelSwtichJob()
        homeFragmentPresenter.getBanner()
        homeFragmentPresenter.getHomeList()
    }

    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener{ _, _, position ->
        if (datas.size != 0)
        {
            Intent(activity, ContentActivity::class.java).run {
                putExtra(Constant.CONTENT_URL_KEY, datas[position].link)
                putExtra(Constant.CONTENT_ID_KEY, datas[position].id)
                putExtra(Constant.CONTENT_TITLE_KEY, datas[position].title)
                startActivity(this)
            }
        }

    }

    private val onBannerItemClickListener = BaseQuickAdapter.OnItemClickListener{_, _, position ->
        if (bannerDatas.size != 0)
        {
            Intent(activity, ContentActivity::class.java).run {
                putExtra(Constant.CONTENT_URL_KEY, bannerDatas[position].url)
                putExtra(Constant.CONTENT_TITLE_KEY, bannerDatas[position].title)
                startActivity(this)
            }
        }
    }

    private val onRequestLoadMoreListener = BaseQuickAdapter.RequestLoadMoreListener {
        val page = homeAdapter.data.size / 20 + 1
        homeFragmentPresenter.getHomeList(page)
    }

    private val onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener{_, view, position ->
        if (datas.size != 0)
        {
            val data = datas[position]

            when (view.id)
            {
                R.id.homeItemType ->
                {
                    data.chapterName?:let {
                        activity.toast(getString(R.string.type_null))
                        return@OnItemChildClickListener
                    }

                    Intent(activity, TypeContentActivity::class.java).run {
                        putExtra(Constant.CONTENT_TARGET_KEY, true)
                        putExtra(Constant.CONTENT_TITLE_KEY, data.chapterName)
                        putExtra(Constant.CONTENT_CID_KEY, data.chapterId)
                        startActivity(this)
                    }
                }
                R.id.homeItemLike ->
                {
                    if (isLogin)
                    {
                        val collect = data.collect
                        data.collect = !collect
                        homeAdapter.setData(position, data)
                        homeFragmentPresenter.collectArticle(data.id, !collect)
                    }
                    else
                    {
                        Intent(activity, LoginActivity::class.java).run {
                            startActivityForResult(this, Constant.MAIN_REQUEST_CODE)
                        }
                        activity.toast(getString(R.string.login_please_login))
                    }
                }
            }
        }
    }

    private val onScrollListener = object : RecyclerView.OnScrollListener()
    {
        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            when (newState)
            {
                RecyclerView.SCROLL_STATE_IDLE ->
                {
                    currentIndex = linearLayoutManager.findFirstVisibleItemPosition()
                    startSwitchJob()
                }
            }
        }
    }



    private val onTouchListener = View.OnTouchListener{
        _, event ->
        when (event.action)
        {
            MotionEvent.ACTION_MOVE ->
            {
                cancelSwtichJob()
            }
        }
        false
    }

    private fun getBannerSwitchJob() = launch {
        repeat(Int.MAX_VALUE)
        {
            if (bannerDatas.size == 0)
            {
                return@launch
            }
            delay(BANNER_TIME)
            currentIndex++
            val index = currentIndex % bannerDatas.size
            bannerRecyclerView.smoothScrollToPosition(index)
            currentIndex = index
        }
    }



    private fun startSwitchJob() = bannerSwitchJob?.run {
        if (!isActive)
        {
            bannerSwitchJob = getBannerSwitchJob().apply {
                start()
            }
        }
    }?:let {
        bannerSwitchJob = getBannerSwitchJob().apply {
            start()
        }
    }

    private fun cancelSwtichJob() = bannerSwitchJob?.run {
        if (isActive)
        {
            cancel()
        }
    }
}