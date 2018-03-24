package com.hsf1002.sky.wanandroid.presenter

import com.hsf1002.sky.wanandroid.bean.*

/**
 * Created by hefeng on 18-3-24.
 */

interface HomePresenter
{
    interface OnHomeListListener
    {
        fun getHomeList(page:Int = 0)
        fun getHomeListSuccess(result:HomeListResponse)
        fun getHomeListFailed(errorMsg:String?)
    }

    interface OnTypeTreeListListener
    {
        fun getTypeTreeList()
        fun getTypeTreeListSuccess(result: TreeListResponse)
        fun getTypeTreeListFailed(errorMsg:String?)
    }

    interface OnLoginListener
    {
        fun loginWanAndroid(username:String, password:String)
        fun loginSuccess(result: LoginResponse)
        fun loginFailed(errorMsg:String)
    }

    interface OnRegisterListener
    {
        fun registerWanAndroid(username:String, password:String, repassword:String)
        fun registerSuccess(result:LoginResponse)
        fun registerFailed(errorMsg: String)
    }

    interface OnFriendListListener
    {
        fun getFriendList(bookmarkResult:FriendListResponse?, commonResult:FriendListResponse, hotResult:HotKeyResponse)
        fun getFriendListSuccess()
        fun getFriendListFailed(errorMsg: String)
    }

    interface OnCollectArticleListener
    {
        fun collectArticle(id:Int, isAdd:Boolean)
        fun collectArticleSuccess(result:HomeListResponse, isAdd:Boolean)
        fun collectArticleFailed(errorMsg: String, isAdd:Boolean)
    }

    interface OnCollectOutsideArticleListener
    {
        fun collectOutSideArticle(title:String, author:String, link:String, isAdd:Boolean)
        fun collectOutSideArticleSuccess(result:HomeListResponse, isAdd:Boolean)
        fun collectOutSideArticleFailed(errorMsg: String, isAdd:Boolean)
    }

    interface OnBannerListener
    {
        fun getBanner()
        fun getBannerSuccess(result:BannerResponse)
        fun getBannerFailed(errorMsg: String)
    }

    interface OnBookmarkListListener
    {
        fun getFriendList()
        fun getFriendListSuccess(result:FriendListResponse)
        fun getFriendListFailed(errorMsg: String)
    }
}