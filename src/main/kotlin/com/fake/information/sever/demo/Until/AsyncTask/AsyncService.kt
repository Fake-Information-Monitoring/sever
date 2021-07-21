package com.fake.information.sever.demo.Until.AsyncTask

import org.springframework.util.concurrent.ListenableFuture
import java.util.concurrent.Future

interface AsyncService {
    /**
     * 异步调用，无返回值
     */
    fun asyncTask(func:()->Unit)

    /**
     * 异步调用，有返回值
     */
    fun <T>asyncTaskReturn(func:()->Unit): ListenableFuture<T>
}