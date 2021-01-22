package com.fake.information.sever.demo.Http.Response

open class Result<T> {
    //是否成功
    public var success: Boolean? = null
    //状态码
    public var code: Int? = null
    //提示信息
    public var msg: String? = null
    //数据
    public var data: T? = null

    constructor(success: Boolean?, code: Int?, msg: String?, data: T?) {
        this.success = success
        this.code = code
        this.msg = msg
        this.data = data
    }

    companion object {
        //TODO: 自定义异常返回的结果
        //TODO:其他异常处理方法返回的结果
    }
}