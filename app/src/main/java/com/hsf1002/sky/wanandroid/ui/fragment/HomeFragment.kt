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
import android.widget.HorizontalScrollView
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
            setOnItemClickListener(onItemClickListener)
            setOnItemChildClickListener(onItemChildClickListener)
            addHeaderView(bannerRecyclerView)
            setEmptyView(R.layout.fragment_home_empty)
        }

//        homeFragmentPresenter.getBanner()
 //       homeFragmentPresenter.getHomeList()
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
        //homeFragmentPresenter
    }

    override fun collectArticleSuccess(result: HomeListResponse, isAdd: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getHomeListSuccess(result: HomeListResponse) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun collectArticleFailed(errorMsg: String?, isAdd: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getHomeListFailed(errorMsg: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getHomeListZero() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getHomeListSmall(result: HomeListResponse) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBannerSuccess(result: BannerResponse) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBannerFailed(errorMsg: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBannerZero() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        refreshData()
    }

    fun refreshData()
    {

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