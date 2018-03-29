package com.hsf1002.sky.wanandroid.view

import com.hsf1002.sky.wanandroid.bean.FriendListResponse
import com.hsf1002.sky.wanandroid.bean.HotKeyResponse

/**
 * Created by hefeng on 18-3-29.
 */
interface CommonUseFragmentView {
    fun getFriendListSuccess(bookmarkResult:FriendListResponse?, commonResult:FriendListResponse?, hotResult:HotKeyResponse)
    fun getFriendListFailed(errorMsg:String?)
    fun getFriendListZero()
}
