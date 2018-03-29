package com.hsf1002.sky.wanandroid.model

import com.hsf1002.sky.wanandroid.bean.*
import com.hsf1002.sky.wanandroid.cancelByActive
import com.hsf1002.sky.wanandroid.constant.Constant
import com.hsf1002.sky.wanandroid.presenter.HomePresenter
import com.hsf1002.sky.wanandroid.retrofit.RetrofitHelper
import com.hsf1002.sky.wanandroid.tryCatch
import com.hsf1002.sky.wanandroid.view.CollectArticleView
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async

/**
 * Created by hefeng on 18-3-26.
 */


class HomeModelImpl:HomeModel, CollectArticleModel
{
    // home list
    private var homeListAsync:Deferred<HomeListResponse> ? = null
    // tree
    private var typeTreeListAsync:Deferred<TreeListResponse> ? = null
    private var logginAsync:Deferred<LoginResponse> ? = null
    private var registerAsync:Deferred<LoginResponse> ? = null
    private var collectArticleAsync:Deferred<HomeListResponse> ? = null
    // banner
    private var bannerAsync:Deferred<BannerResponse> ? = null

    // my bookmark
    private var bookmarkListAsync:Deferred<FriendListResponse> ? = null
    // everyone search
    private var hotListAsync:Deferred<HotKeyResponse> ? = null
    // common use website
    private var friendListAsync:Deferred<FriendListResponse> ? = null


    override fun getHomeList(onHomeListListener: HomePresenter.OnHomeListListener, page: Int) {
        async(UI)
        {
            tryCatch ({
              it.printStackTrace()
                onHomeListListener.getHomeListFailed(it.toString())
            })
            {
                homeListAsync.cancelByActive()
                homeListAsync = RetrofitHelper.retrofitService.getHomeList(page)

                val result = homeListAsync?.await()
                result ?: let {
                    onHomeListListener.getHomeListFailed(Constant.RESULT_NULL)
                    return@async
                }
                onHomeListListener.getHomeListSuccess(result)
            }
        }

    }

    override fun cancelHomeListRequest() {
        homeListAsync?.cancelByActive()
    }

    override fun collectArticle(onCollectArticleListener: HomePresenter.OnCollectArticleListener, id: Int, isAdd: Boolean) {
        async(UI) {
            tryCatch( {
                it.printStackTrace()
                onCollectArticleListener.collectArticleFailed(it.toString(), isAdd)
            } )
            {
                collectArticleAsync.cancelByActive()
                collectArticleAsync = if (isAdd)
                {
                    RetrofitHelper.retrofitService.addCollectArticle(id)
                }
                else
                {
                    RetrofitHelper.retrofitService.removeCollectArticle(id)
                }

                val result = collectArticleAsync?.await()
                result?:let {
                    onCollectArticleListener.collectArticleFailed(Constant.RESULT_NULL, isAdd)
                    return@async
                }

                onCollectArticleListener.collectArticleSuccess(result, isAdd)
            }
        }
    }

    override fun cancelCollectRequest() {
        collectArticleAsync?.cancelByActive()
    }

    override fun getTypeTreeList(onTypeTreeListListener: HomePresenter.OnTypeTreeListListener) {
        async(UI) {
            tryCatch( {
                it.printStackTrace()
                onTypeTreeListListener.getTypeTreeListFailed(it.toString())
            })
            {
                typeTreeListAsync?.cancelByActive()
                typeTreeListAsync = RetrofitHelper.retrofitService.getTypeTreeList()

                val result = typeTreeListAsync?.await()
                result?:let {
                    onTypeTreeListListener.getTypeTreeListFailed(Constant.RESULT_NULL)
                    return@async
                }

                onTypeTreeListListener.getTypeTreeListSuccess(result)
            }
        }

    }

    override fun cancelTypeTreeRequest() {
        typeTreeListAsync?.cancelByActive()
    }

    override fun loginWanAndroid(onLoginListener: HomePresenter.OnLoginListener, username: String, password: String) {
        async(UI) {
            tryCatch( {
                it.printStackTrace()
                onLoginListener.loginFailed(it.toString())
            })
            {
                logginAsync?.cancelByActive()
                logginAsync = RetrofitHelper.retrofitService.loginWanAndroid(username, password)

                val result = logginAsync?.await()
                result?:let {
                    onLoginListener.loginFailed(Constant.RESULT_NULL)
                    return@async
                }

                onLoginListener.loginSuccess(result)
            }
        }
    }

    override fun cancelLoginRequest() {
        logginAsync?.cancelByActive()
    }

    override fun registerWanAndroid(onRegisterListener: HomePresenter.OnRegisterListener, username: String, password: String, repassword: String) {
        async(UI) {
            tryCatch ({
                it.printStackTrace()
                onRegisterListener.registerFailed(it.toString())
            })
            {
                registerAsync?.cancelByActive()
                registerAsync = RetrofitHelper.retrofitService.registerWanAndroid(username, password, repassword)

                val result = registerAsync?.await()
                result?:let {
                    onRegisterListener.registerFailed(Constant.RESULT_NULL)
                    return@async
                }

                onRegisterListener.registerSuccess(result)
            }
        }
    }

    override fun cancelRegisterRequest() {
        registerAsync?.cancelByActive()
    }

    override fun getFriendList(onFriendListListener: HomePresenter.OnFriendListListener) {
        var throwable:Throwable? = null
        var bookmarkResult:FriendListResponse? = null
        var hotResult:HotKeyResponse? = null
        var commonREsult:FriendListResponse? = null

        async(UI) {
            tryCatch ({
                throwable = it
                it.printStackTrace()
            })
            {
                bookmarkListAsync?.cancelByActive()
                bookmarkListAsync = RetrofitHelper.retrofitService.getBookmarkList()

                val result = bookmarkListAsync?.await()
                result?.let {
                    bookmarkResult = it
                }
            }

            tryCatch ({
                throwable = it
                it.printStackTrace()
            })
            {
                hotListAsync?.cancelByActive()
                hotListAsync = RetrofitHelper.retrofitService.getHotKeyList()

                val result = hotListAsync?.await()
                result?.let {
                    hotResult   = it
                }
            }

            tryCatch ({
                throwable = it
                it.printStackTrace()
            })
            {
                friendListAsync?.cancelByActive()
                friendListAsync = RetrofitHelper.retrofitService.getFriendList()

                val result = friendListAsync?.await()
                result?.let {
                    commonREsult   = it
                }
            }

            throwable?.let {
                onFriendListListener.getFriendListFailed(it.toString())
                //return@async
            }

            hotResult?.let {
                onFriendListListener.getFriendListFailed(Constant.RESULT_NULL)
            }

            commonREsult?.let {
                onFriendListListener.getFriendListFailed(Constant.RESULT_NULL)
            }

            onFriendListListener.getFriendListSuccess(bookmarkResult, commonREsult!!, hotResult!!)
        }

    }

    override fun cancelFriendRequest() {
        bookmarkListAsync?.cancelByActive()
        hotListAsync?.cancelByActive()
        friendListAsync?.cancelByActive()
    }

    override fun getBanner(onBannerListener: HomePresenter.OnBannerListener) {
        async(UI) {
            tryCatch ({
                it.printStackTrace()
                onBannerListener.getBannerFailed(it.toString())
            })
            {
                bannerAsync?.cancelByActive()
                bannerAsync = RetrofitHelper.retrofitService.getBanner()

                val result = bannerAsync?.await()
                result?:let {
                    onBannerListener.getBannerFailed(Constant.RESULT_NULL)
                    return@async
                }

                onBannerListener.getBannerSuccess(result)
            }
        }
    }

    override fun cancelBannerRequest() {
        bannerAsync?.cancelByActive()
    }
}