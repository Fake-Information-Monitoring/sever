package com.fake.information.sever.demo.Until.AsyncTask


import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.AsyncResult
import org.springframework.stereotype.Service
import java.util.concurrent.Future

@Service
class FakeNewsAsyncServiceImpl : AsyncService {

    @Async
    override fun asyncTask(func: () -> Unit) {
        func()
    }

    @Async("asyncTaskExecutor")
    override fun <T> asyncTaskReturn(func: () -> Unit): Future<T> {
        val a = func()
        return AsyncResult.forValue<T>(a as T)
    }
}