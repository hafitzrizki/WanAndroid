package com.hsf1002.sky.wanandroid.bean

/**
 * Created by hefeng on 18-3-24.
 */

import java.io.Serializable

data class TreeListResponse(
        var errorCode: Int,
        var errorMsg: String?,
        var data: List<Data>
) {
    data class Data(
            var id: Int,
            var name: String,
            var courseId: Int,
            var parentChapterId: Int,
            var order: Int,
            var visible: Int,
            var children: List<Children>?
    ) : Serializable {
        data class Children(
                var id: Int,
                var name: String,
                var courseId: Int,
                var parentChapterId: Int,
                var order: Int,
                var visible: Int,
                var children: List<Children>?
        ) : Serializable
    }
}