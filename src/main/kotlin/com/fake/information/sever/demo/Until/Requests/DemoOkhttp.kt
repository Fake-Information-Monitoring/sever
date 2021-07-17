package com.fake.information.sever.demo.Until.Requests

import com.fake.information.sever.demo.Model.VerifyBaseModel
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.*
import java.util.*


object DemoOkhttp {
    inline fun post(url:String,header:Map<String,String>?=null):VerifyBaseModel{
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
        return  Gson().fromJson(responseBody,VerifyBaseModel::class.java);
    }
}

