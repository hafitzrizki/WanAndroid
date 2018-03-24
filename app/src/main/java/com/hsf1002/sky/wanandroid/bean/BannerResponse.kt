package com.hsf1002.sky.wanandroid.bean

/**
 * Created by hefeng on 18-3-24.
 */

data class BannerResponse(
        var errorCode: Int,
        var errorMsg: String?,
        var data: List<Data>?
)
{
    data class Data(
            var id: Int,
            var url: String,
            var imagePath: String,
            var title: String,
            var desc: String,
            var isVisible: Int,
            var order: Int,
            var `type`: Int
    )
}