package com.hsf1002.sky.wanandroid.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.hsf1002.sky.wanandroid.R
import com.hsf1002.sky.wanandroid.adapter.TypeArticleAdapter
import com.hsf1002.sky.wanandroid.base.BaseFragment
import com.hsf1002.sky.wanandroid.base.Preference
import com.hsf1002.sky.wanandroid.bean.ArticleListResponse
import com.hsf1002.sky.wanandroid.bean.Datas
import com.hsf1002.sky.wanandroid.bean.HomeListResponse
import com.hsf1002.sky.wanandroid.constant.Constant
import com.hsf1002.sky.wanandroid.presenter.TypeArticlePresenterImpl
import com.hsf1002.sky.wanandroid.toast
import com.hsf1002.sky.wanandroid.ui.activity.ContentActivity
import com.hsf1002.sky.wanandroid.ui.activity.LoginActivity
import com.hsf1002.sky.wanandroid.view.CollectArticleView
import com.hsf1002.sky.wanandroid.view.TypeArticleFragmentView
import kotlinx.android.synthetic.main.fragment_type_content.*

/**
 * Created by hefeng on 18-3-27.
 */
class TypeArticleFragment:BaseFragment(), TypeArticleFragmentView, CollectArticleView {

    private var mainView: View? = null
    private val datas = mutableListOf<Datas>()
    private val typeArticlePresenter:TypeArticlePresenterImpl by lazy {
        TypeArticlePresenterImpl(this, this)
    }
    private val typeArticleAdapter:TypeArticleAdapter by lazy {
        TypeArticleAdapter(activity, datas)
    }
    private var cid:Int = 0
    private val isLogin:Boolean by Preference(Constant.LOGIN_KEY, false)


    companion object {
        fun newInstance(cid:Int):TypeArticleFragment
        {
            val fragment = TypeArticleFragment()
            val args = Bundle()
            args.putInt(Constant.CONTENT_CID_KEY, cid)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mainView ?: let {
            mainView = inflater?.inflate(R.layout.fragment_type_content, container, false)
        }
        return mainView//super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        cid = arguments.getInt(Constant.CONTENT_CID_KEY)

        tabSwipeRefreshLayout.run {
            isRefreshing = true
            setOnRefreshListener(onRefreshListener)
        }

        tabRecyclerView.run {
            layoutManager = LinearLayoutManager(activity)
            adapter = typeArticleAdapter
        }

        typeArticleAdapter.run {
            setOnLoadMoreListener(onRequestLoadMoreListener, tabRecyclerView)
            //setOnItemClickListener(this@TypeArticleFragment.onItemClickListener)
            onItemClickListener = this@TypeArticleFragment.onItemClickListener
            //setOnItemChildClickListener(this@TypeArticleFragment.onItemChildClickListener)
            onItemChildClickListener = this@TypeArticleFragment.onItemChildClickListener
            setEmptyView(R.layout.fragment_home_empty)
        }

        typeArticlePresenter.getTypeArticleList(cid = cid)
    }

    override fun cancelRequest() {
        typeArticleAdapter.loadMoreComplete()
        typeArticlePresenter.cancelRequest()
        tabSwipeRefreshLayout.isRefreshing = false
    }

    override fun collectArticleSuccess(result: HomeListResponse, isAdd: Boolean) {
        activity.toast(
                if (isAdd)
                    activity.getString(R.string.bookmark_success)
                else
                    activity.getString(R.string.bookmark_cancel_success)
        )
    }

    override fun collectArticleFailed(errorMsg: String?, isAdd: Boolean) {
        activity.toast(
                if (isAdd)
                    activity.getString(R.string.bookmark_failed)
                else
                    activity.getString(R.string.bookmark_cancel_failed)
        )
    }

    override fun getTypeArticleListSuccess(result: ArticleListResponse) {
        result.data.datas?.let {
            typeArticleAdapter.run {
                val total = result.data.total

                if (result.data.offset >= total || data.size >= total)
                {
                    loadMoreEnd()
                    return@let
                }

                if (tabSwipeRefreshLayout.isRefreshing)
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
    }

    override fun getTypeArticleListFailed(errorMsg: String?) {
        typeArticleAdapter.setEnableLoadMore(false)
        typeArticleAdapter.loadMoreFail()

        errorMsg?.let {
            activity.toast(it)
        } ?: let {
            activity.toast(getString(R.string.get_data_error))
        }

        tabSwipeRefreshLayout.isRefreshing = false
    }

    override fun getTypeArticleListZero() {
        activity.toast(getString(R.string.get_data_zero))
        tabSwipeRefreshLayout.isRefreshing = false
    }

    override fun getTypeArticleListSmall(result: ArticleListResponse) {
        result.data.datas?.let {
            typeArticleAdapter.run {
                replaceData(it)
                loadMoreComplete()
                loadMoreEnd()
                setEnableLoadMore(false)
            }
        }

        tabSwipeRefreshLayout.isRefreshing = false
    }

    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        tabSwipeRefreshLayout.isRefreshing = true
        typeArticleAdapter.setEnableLoadMore(false)
        typeArticlePresenter.getTypeArticleList(cid = cid)
    }

    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
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

    private val onRequestLoadMoreListener = BaseQuickAdapter.RequestLoadMoreListener {
        val page = typeArticleAdapter.data.size / 20 + 1
        typeArticlePresenter.getTypeArticleList(page, cid)
    }

    private val onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener{_, view, position ->
        if (datas.size != 0)
        {
            val data = datas[position]

            when (view.id)
            {
                R.id.homeItemLike ->
                {
                    if (isLogin)
                    {
                        val collect = data.collect
                        data.collect = !collect
                        typeArticleAdapter.setData(position, data)
                        typeArticlePresenter.collectArticle(data.id, !collect)
                    }
                    else{
                        Intent(activity, LoginActivity::class.java).run {
                            startActivity(this)
                        }
                        activity.toast(getString(R.string.login_please_login))
                    }
                }
            }
        }
    }
}