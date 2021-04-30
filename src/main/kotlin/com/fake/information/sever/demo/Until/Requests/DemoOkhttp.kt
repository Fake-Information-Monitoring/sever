package com.fake.information.sever.demo.Until.Requests

import com.google.gson.GsonBuilder
import okhttp3.*

object DemoOkhttp {
    inline fun <reified T> post(url:String,header:Map<String,String>?=null):T{
        val client= OkHttpClient()
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
        return GsonBuilder().create()
            .fromJson(responseBody, T::class.java)
    }
}

