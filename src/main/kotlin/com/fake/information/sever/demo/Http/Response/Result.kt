package com.fake.information.sever.demo.Http.Response

import com.google.gson.Gson

open class Result<T>(//是否成功
        public var success: Boolean?,//状态码
        public var code: Int?,//提示信息
        public var msg: String?,//数据
        public var data: T?) {

    fun toJson():String{
        val gson: Gson = Gson()
        return gson.toJson(this)
    }

    companion object {

        //TODO: 自定义异常返回的结果
        //TODO:其他异常处理方法返回的结果
    }
}