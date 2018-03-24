package com.hsf1002.sky.wanandroid.bean

/**
 * Created by hefeng on 18-3-24.
 */


data class HotKeyResponse(
        var errorCode: Int,
        var errorMsg: String?,
        var data: List<Data>?
) {
    data class Data(
            var id: Int,
            var name: String,
            var link: Any,
            var visible: Int,
            var order: Int
    )
}