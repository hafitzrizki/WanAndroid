package com.hsf1002.sky.wanandroid.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.hsf1002.sky.wanandroid.R
import com.hsf1002.sky.wanandroid.bean.HotKeyResponse
import com.hsf1002.sky.wanandroid.getRandomColor
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.activity_main.view.*

/**
 * Created by hefeng on 18-3-29.
 */
class HotTagAdapter(context: Context, datas:List<HotKeyResponse.Data>)
    :TagAdapter<HotKeyResponse.Data>(datas)
{
    private val inflater:LayoutInflater = LayoutInflater.from(context)

    override fun getView(parent: FlowLayout?, position: Int, t: HotKeyResponse.Data?): View {
        return (inflater.inflate(R.layout.common_hot_item, parent, false) as TextView).apply {
            text = t?.name
            val parseColor = try {
                Color.parseColor(getRandomColor())
            }
            catch (_:Exception)
            {
                @Suppress("DEPRECATION")
                content.resources.getColor(R.color.colorPrimary)
            }
            setTextColor(parseColor)
        }
    }
}