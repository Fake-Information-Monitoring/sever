package com.fake.information.sever.demo.Until

import com.google.gson.GsonBuilder
import okhttp3.*

object DemoOkhttp {

    inline fun <reified T> post(text:String, url:String):T{
        val client= OkHttpClient()
        val formBody = FormBody.Builder()
                .add("text",text)
                .build()
        val requestBody = Request.Builder()
                .url(url)
                .post(formBody)
                .build()
        val responseBody = client.newCall(requestBody).execute().body()?.string()
        return GsonBuilder().create().fromJson(responseBody, T::class.java)
    }
}

