package com.fake.information.sever.demo.Until

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

import org.springframework.core.task.AsyncTaskExecutor


@Configuration
class AsyncConfig {
    @Bean("asyncTaskExecutor")
    fun asyncTaskExecutor(): AsyncTaskExecutor {
        val asyncTaskExecutor = ThreadPoolTaskExecutor()
        asyncTaskExecutor.maxPoolSize = MAX_POOL_SIZE
        asyncTaskExecutor.corePoolSize = CORE_POOL_SIZE
        asyncTaskExecutor.setThreadNamePrefix("async-task-thread-pool-")
        asyncTaskExecutor.initialize()
        return asyncTaskExecutor
    }

    companion object {
        private const val MAX_POOL_SIZE = 50
        private const val CORE_POOL_SIZE = 20
    }
}