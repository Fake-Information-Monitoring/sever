package com.fake.information.sever.demo.Until.ThreadPool



import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newFixedThreadPoolContext

object FakeNewsThreadPool {
        @ObsoleteCoroutinesApi
        val threadPool = newFixedThreadPoolContext(10,"ThreadPool")
}