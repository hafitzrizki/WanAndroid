package com.hsf1002.sky.wanandroid.adapter

import android.content.Context
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hsf1002.sky.wanandroid.R
import com.hsf1002.sky.wanandroid.bean.FriendListResponse

/**
 * Created by hefeng on 18-3-29.
 */
class CommonAdapter (val context:Context, datas:MutableList<FriendListResponse.Data>)
    :BaseQuickAdapter<FriendListResponse.Data, BaseViewHolder>(R.layout.common_list_item, datas)
{

    override fun convert(helper: BaseViewHolder?, item: FriendListResponse.Data?) {
        item?:return

        helper?.setText(R.id.commonItemTitle, item.name.trim())
    }
}