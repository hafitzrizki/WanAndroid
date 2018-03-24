package com.hsf1002.sky.wanandroid.base

import android.app.Application
import android.content.ComponentCallbacks2
import com.bumptech.glide.Glide
import com.hsf1002.sky.wanandroid.BuildConfig
import com.squareup.leakcanary.LeakCanary

/**
 * Created by hefeng on 18-3-24.
 */


class BaseApplication:Application()
{
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG)
        {
            LeakCanary.install(this)
        }
        Preference.setContext(applicationContext)
    }

    /*
    *  ---------------后台进程---------------
    * TRIM_MEMORY_COMPLETE：内存不足，并且该进程在后台进程列表最后一个，马上就要被清理
    * TRIM_MEMORY_MODERATE：内存不足，并且该进程在后台进程列表的中部。
    * TRIM_MEMORY_BACKGROUND：内存不足，并且该进程是后台进程。
    * TRIM_MEMORY_UI_HIDDEN：内存不足，并且该进程的UI已经不可见了。
    * 以上4个是4.0增加
    *
    * ---------------前台进程---------------
    * TRIM_MEMORY_RUNNING_CRITICAL：内存不足(后台进程不足3个)，并且该进程优先级比较高，需要清理内存
    * TRIM_MEMORY_RUNNING_LOW：内存不足(后台进程不足5个)，并且该进程优先级比较高，需要清理内存
    * TRIM_MEMORY_RUNNING_MODERATE：内存不足(后台进程超过5个)，并且该进程优先级比较高，需要清理内存
    * 以上3个是4.1增加
    系统也提供了一个ComponentCallbacks2，通过Context.registerComponentCallbacks()注册后，就会被系统回调到。
*/
    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)

        // 内存不足，并且该进程的UI已经不可见了
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN)
        {
            Glide.get(this).clearMemory()
        }
        Glide.get(this).trimMemory(level)
    }

    /*
    * 1，OnLowMemory被回调时，已经没有后台进程；而onTrimMemory被回调时，还有后台进程
    * 2，OnLowMemory是在最后一个后台进程被杀时调用，一般情况是low memory killer 杀进程后触发；而OnTrimMemory的触发更频繁，每次计算进程优先级时，只要满足条件，都会触发
    * 3，通过一键清理后，OnLowMemory不会被触发，而OnTrimMemory会被触发一次
    */
    override fun onLowMemory() {
        super.onLowMemory()

        Glide.get(this).clearMemory()
    }
}