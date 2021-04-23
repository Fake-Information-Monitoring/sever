package com.fake.information.sever.demo.MQ

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.Queue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.amqp.core.BindingBuilder

import org.springframework.amqp.core.TopicExchange




@Configuration
class RabbitMQConfig {
    @Bean
    fun persistenceQueue(): Queue {
        return Queue("",true)
    }
    @Bean
    fun tempQueue():Queue {
        return Queue("")
    }

    @Bean
    fun topicExchange(): TopicExchange? {
        return TopicExchange("topicExchange")
    }

    //绑定
    @Bean
    fun persistenceBinding(): Binding? {
        return BindingBuilder.bind(persistenceQueue()).to(topicExchange()).with("key.1")
    }

    @Bean
    fun tempBinding(): Binding? {
        return BindingBuilder.bind(tempQueue()).to(topicExchange()).with("key.#")
    }
}