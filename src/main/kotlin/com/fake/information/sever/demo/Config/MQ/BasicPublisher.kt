package com.fake.information.sever.demo.Config.MQ

import java.io.UnsupportedEncodingException

import org.springframework.amqp.core.MessageDeliveryMode

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter

import org.springframework.beans.factory.annotation.Autowired

import org.springframework.amqp.rabbit.core.RabbitTemplate

import lombok.extern.slf4j.Slf4j
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessageBuilder
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.log


@Component
@Slf4j
class BasicPublisher {
    @Autowired
    private val rabbitTemplate: RabbitTemplate? = null

    @Autowired
    lateinit var env: Environment

    // 发送字符串类型的消息
    fun sendMsg(messageStr: String) {
        if (messageStr.isNotEmpty()) {
            try {
                rabbitTemplate!!.messageConverter = Jackson2JsonMessageConverter()
                rabbitTemplate.setExchange(env.getProperty("mq.basic.info.exchange.name"))
                rabbitTemplate.routingKey = env.getProperty("mq.basic.info.routing.key.name").toString()

                // 2创建队列、交换机、消息 设置持久化模式
                // 设置消息的持久化模式
                val message: Message = MessageBuilder.withBody(messageStr.toByteArray(charset("utf-8")))
                    .setDeliveryMode(MessageDeliveryMode.PERSISTENT).build()
                rabbitTemplate.convertAndSend(message)
                println("基本消息模型-生产者-发送消息：$messageStr")
            } catch (e: UnsupportedEncodingException) {
                println("基本消息模型-生产者-发送消息发生异常：$messageStr, $e.fillInStackTrace()", )
            }
        }
    }
}