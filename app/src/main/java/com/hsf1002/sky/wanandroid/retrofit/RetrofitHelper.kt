package com.hsf1002.sky.wanandroid.retrofit

import com.hsf1002.sky.wanandroid.base.Preference
import com.hsf1002.sky.wanandroid.constant.Constant
import com.hsf1002.sky.wanandroid.encodeCookie
import com.hsf1002.sky.wanandroid.loge
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Created by hefeng on 18-3-26.
 */
object RetrofitHelper {
    private const val TAG = "RetrofitHelper"
    private const val CONTENT_PRE = "OkHttp: "
    private const val SAVE_USER_LOGIN_KEY = "user/login"
    private const val SAVE_USER_REGISTER_KEY = "user/register"
    private const val SET_COOKIE_KEY = "set-cookie"
    private const val COOKIE_NAME = "Cookie"
    private const val CONNECT_TIMEOUT = 30L
    private const val READ_TIMEOUT = 10L

    val retrofitService: RetrofitService = RetrofitHelper.getService(Constant.REQUEST_BASE_URL, RetrofitService::class.java)


    private fun create(url:String):Retrofit
    {
        val okHttpClientBuilder = OkHttpClient().newBuilder().apply {
            connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)

            addInterceptor {
                val request = it.request()
                val response = it.proceed(request)
                val url = request.url().toString()
                val domain = request.url().host()

                if ((url.contains(SAVE_USER_LOGIN_KEY) || url.contains(SAVE_USER_REGISTER_KEY)) && !response.headers(SET_COOKIE_KEY).isEmpty())
                {
                    val cookies = response.headers(SET_COOKIE_KEY)
                    val cookie = encodeCookie(cookies)
                    saveCookie(url, domain, cookie)
                }
                response
            }

            addInterceptor {
                val request = it.request()
                val builder = request.newBuilder()
                val url = request.url().toString()
                val domain = request.url().host()

                if (domain.isNotEmpty())
                {
                    val spDomain:String by Preference(domain, "")
                    val cookie:String = if (spDomain.isNotEmpty()) spDomain else ""

                    if (cookie.isNotEmpty())
                    {
                        builder.addHeader(COOKIE_NAME, cookie)
                    }
                }
                it.proceed(builder.build())
            }

            if (Constant.INTERCEPTOR_ENABLE)
            {
                addInterceptor(
                    HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                        loge(TAG, CONTENT_PRE + it)
                    }).apply {
                        setLevel(HttpLoggingInterceptor.Level.BODY)
                    })
                }
            }


        return RetrofitBuild(
                url = url,
                client = okHttpClientBuilder.build(),
                gsonFactory = GsonConverterFactory.create(),
                coroutineCallAdapterFactory = CoroutineCallAdapterFactory())
                .retrofit
    }

    private fun<T> getService(url:String, service:Class<T>):T = create(url).create(service)

    private fun saveCookie(url: String, domain:String, cookies:String)
    {
        url ?: return

        var spUrl:String by Preference(url, cookies)
        spUrl = cookies

        domain ?: return

        var spDomain:String by Preference(domain, cookies)
        spDomain = cookies
    }

    class RetrofitBuild(url:String, client:OkHttpClient, gsonFactory:GsonConverterFactory, coroutineCallAdapterFactory: CoroutineCallAdapterFactory)
    {
        val retrofit:Retrofit = Retrofit.Builder().apply {
            baseUrl(url)
            client(client)
            addConverterFactory(gsonFactory)
            addCallAdapterFactory(coroutineCallAdapterFactory)
        }.build()
    }

}