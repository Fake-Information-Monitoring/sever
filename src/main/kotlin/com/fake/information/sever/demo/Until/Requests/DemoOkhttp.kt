package com.fake.information.sever.demo.Until.Requests

import com.fake.information.sever.demo.Model.VerifyBaseModel
import com.google.gson.Gson
import okhttp3.*
import java.util.*
import java.util.concurrent.TimeUnit


object DemoOkhttp {
    private const val READ_TIMEOUT = 100;

    private const val CONNECT_TIMEOUT = 60;

    private const val WRITE_TIMEOUT = 60;
    var client= OkHttpClient()
    fun post(url:String,header:Map<String,String>?=null):VerifyBaseModel{
        val builder = client.newBuilder()
        builder.readTimeout(READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
        //连接超时
        builder.connectTimeout(CONNECT_TIMEOUT.toLong(), TimeUnit.SECONDS);
        //写入超时
        builder.writeTimeout(WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS);
        builder.connectionPool(ConnectionPool(32, 5, TimeUnit.MINUTES))
        client = builder.build()
        val formBodyBuilder = FormBody.Builder()
        header?.forEach{
            formBodyBuilder.add(
                it.key,it.value
            )
        }
        val formBody = formBodyBuilder.build()
        val requestBody = Request.Builder()
                .url(url)
                .post(formBody)
                .build()
        val responseBody = client.newCall(requestBody)
            .execute().body()?.string()
        return  Gson().fromJson(responseBody,VerifyBaseModel::class.java);
    }
}

