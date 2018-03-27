package com.hsf1002.sky.wanandroid.adapter

import android.content.Context
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hsf1002.sky.wanandroid.R
import com.hsf1002.sky.wanandroid.bean.TreeListResponse

/**
 * Created by hefeng on 18-3-26.
 */
class TypeAdapter(val context:Context, val datas:MutableList<TreeListResponse.Data>):
    BaseQuickAdapter<TreeListResponse.Data, BaseViewHolder>(R.layout.type_list_item, datas)
{
    override fun convert(helper: BaseViewHolder?, item: TreeListResponse.Data?) {
        item ?: return // null

        helper?.setText(R.id.typeItemFirst, item.name)

        item.children?.let {
            children ->
                helper?.setText(R.id.typeItemSecond,
                        children.joinToString("    ",  transform = {
                            child ->
                                child.name
                        })
                )
        }
    }
}