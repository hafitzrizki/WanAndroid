package com.hsf1002.sky.wanandroid.bean

/**
 * Created by hefeng on 18-3-24.
 */

data class Data(    var offset: Int,
                    var size: Int,
                    var total: Int,
                    var pageCount: Int,
                    var curPage: Int,
                    var over: Boolean,
                    var datas: List<Datas>?)
{

}