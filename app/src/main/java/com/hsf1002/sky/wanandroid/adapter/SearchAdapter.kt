package com.hsf1002.sky.wanandroid.adapter

import android.content.Context
import android.text.Html
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hsf1002.sky.wanandroid.R
import com.hsf1002.sky.wanandroid.bean.Datas
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by hefeng on 18-3-28.
 */
class SearchAdapter (val context: Context, datas:MutableList<Datas>):
    BaseQuickAdapter<Datas, BaseViewHolder>(R.layout.home_list_item, datas)
{
    override fun convert(helper: BaseViewHolder?, item: Datas?) {
        item ?: return

        @Suppress("DEPRECATION")
        helper!!.setText(R.id.homeItemTitle, Html.fromHtml(item.title))
                .setText(R.id.homeItemAuthor, item.author)
                .setText(R.id.homeItemType, item.chapterName)
                .setText(R.id.homeItemDate,
                        if (item.originId != 0)
                        {
                            context.getString(R.string.bookmark_time, item.niceDate)
                        }
                        else
                        {
                            item.niceDate
                        })
                .setTextColor(R.id.homeItemType, context.resources.getColor(R.color.colorPrimary))
                .addOnClickListener(R.id.homeItemType)
                .linkify(R.id.homeItemType)
                .setImageResource(R.id.homeItemLike,
                    if (item.originId != 0 || item.collect) R.drawable.ic_action_like
                    else R.drawable.ic_action_no_like)
                .addOnClickListener(R.id.homeItemLike)
    }
}