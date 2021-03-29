package com.fake.information.sever.demo.Until.AsyncTask


import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.AsyncResult
import org.springframework.stereotype.Service
import java.util.concurrent.Future

@Service
class FakeNewsAsyncService : AsyncService {

    @Async
    override fun asyncTask(T: () -> Unit) {
        T()
    }

    @Async
    override fun <T> asyncTaskReturn(R: () -> Unit): Future<T> {
        val a = R()
        return AsyncResult.forValue<T>(a as T)
    }


    @Async
    override fun asyncTaskForTransaction(exFlag: Boolean?) {
        TODO("Not yet implemented")
    }
}