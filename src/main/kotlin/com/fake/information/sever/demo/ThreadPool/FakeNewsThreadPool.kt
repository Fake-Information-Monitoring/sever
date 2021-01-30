package com.fake.information.sever.demo.ThreadPool



import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newFixedThreadPoolContext
import java.util.concurrent.BlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

object FakeNewsThreadPool {
        @ObsoleteCoroutinesApi
        val threadPool = newFixedThreadPoolContext(10,"ThreadPool")
}