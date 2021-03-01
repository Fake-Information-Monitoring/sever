package com.fake.information.sever.demo.Until

import com.fake.information.sever.demo.Model.CDKey
import com.fake.information.sever.demo.Model.VerifyTextResult
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.*

class DemoOkhttp {
    private val client= OkHttpClient()

    companion object{
        fun Builder(): DemoOkhttp {
            return DemoOkhttp()
        }
    }

    fun post(text:String,url:String):VerifyTextResult{
        val formBody = FormBody.Builder()
                .add("text",text)
                .build()
        val requestBody = Request.Builder()
                .url(url)
                .post(formBody)
                .build()
        val responseBody = client.newCall(requestBody).execute().body()?.string()
        return GsonBuilder().create().fromJson(responseBody, VerifyTextResult::class.java)
    }
}

