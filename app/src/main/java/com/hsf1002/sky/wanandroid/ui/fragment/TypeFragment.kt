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
import com.hsf1002.sky.wanandroid.adapter.TypeAdapter
import com.hsf1002.sky.wanandroid.base.BaseFragment
import com.hsf1002.sky.wanandroid.bean.TreeListResponse
import com.hsf1002.sky.wanandroid.constant.Constant
import com.hsf1002.sky.wanandroid.presenter.TypeFragmentPresenterImpl
import com.hsf1002.sky.wanandroid.toast
import com.hsf1002.sky.wanandroid.ui.activity.TypeContentActivity
import com.hsf1002.sky.wanandroid.view.TypeFragmentView
import kotlinx.android.synthetic.main.fragment_type.*

/**
 * Created by hefeng on 18-3-24.
 */

class TypeFragment: BaseFragment(), TypeFragmentView
{
    private var mainView: View? = null
    private val datas = mutableListOf<TreeListResponse.Data>()
    private val typeFragmentPresenter: TypeFragmentPresenterImpl by lazy {
        TypeFragmentPresenterImpl(this)
    }
    private val typeAdapter: TypeAdapter by lazy {
        TypeAdapter(activity, datas)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mainView?:let {
            mainView = inflater?.inflate(R.layout.fragment_type, container, false)
        }
        return mainView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        typeSwipeRefreshLayout.run {
            isRefreshing = true
            setOnRefreshListener(onRefreshListener)
        }

        typeRecyclerView.run {
            layoutManager = LinearLayoutManager(activity)
            adapter = typeAdapter
        }

        typeAdapter.run {
            bindToRecyclerView(typeRecyclerView)
            setEmptyView(R.layout.fragment_home_empty)
            onItemClickListener = this@TypeFragment.onItemClickListener // the same function name
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        if (!hidden && isFirst)
        {
            typeFragmentPresenter.getTypeTreeList()
            isFirst = false
        }
    }

    override fun cancelRequest() {
        typeFragmentPresenter.cancelRequest()
    }

    fun smoothScrollToPosition() = typeRecyclerView.smoothScrollToPosition(0)

    override fun getTypeListSuccess(result: TreeListResponse) {
        result.data.let {
            if (typeSwipeRefreshLayout.isRefreshing)
            {
                typeAdapter.replaceData(it)
            }
            else
            {
                typeAdapter.addData(it)
            }
        }
        typeSwipeRefreshLayout.isRefreshing = false
    }

    override fun getTypeListFailed(errorMsg: String?) {
        errorMsg?.let {
            activity.toast(it)
        } ?: let {
            activity.toast(getString(R.string.get_data_error))
        }
        typeSwipeRefreshLayout.isRefreshing = false
    }

    override fun getTypeListZero() {
        activity.toast(getString(R.string.get_data_zero))
        typeSwipeRefreshLayout.isRefreshing = false
    }

    fun refreshData()
    {
        typeSwipeRefreshLayout.isRefreshing = true
        typeFragmentPresenter.getTypeTreeList()
    }

    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        refreshData()
    }

    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener{ _, _, position ->

        if (datas.size != 0)
        {
            Intent(activity, TypeContentActivity::class.java).run {     /////////////////////////// the left bracket must adhere this line
                putExtra(Constant.CONTENT_TITLE_KEY, datas[position].name)
                putExtra(Constant.CONTENT_CHILDREN_DATA_KEY, datas[position])
                startActivity(this)
            }
        }
    }
}