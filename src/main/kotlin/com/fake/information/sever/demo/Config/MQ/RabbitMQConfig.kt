package com.fake.information.sever.demo.Config.MQ

import lombok.extern.slf4j.Slf4j
import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment


@Configuration
@Slf4j
class RabbitMQConfig {
    @Autowired
    lateinit var connectionFactory: CachingConnectionFactory

    @Bean(name = ["basicQueue"])
    fun basicQueue(): Queue? {
        return Queue(env!!.getProperty("mq.basic.info.queue.name"), true)
    }

    // 1.2、创建交换机
    @Bean
    fun basicExchange(): DirectExchange? {
        return DirectExchange(env!!.getProperty("mq.basic.info.exchange.name"), true, false)
    }

    // 1.3、创建绑定关系
    @Bean
    fun basicBinding(): Binding? {
        return BindingBuilder.bind(basicQueue()).to(basicExchange())
            .with(env!!.getProperty("mq.basic.info.routing.key.name"))
    }

    @Bean(name = ["rabbitMQTemplate"])
    fun rabbitTemplate(): RabbitTemplate {
        // 生产者确认消息是否发送过去了
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.NONE)

        // 生产者发送消息后，返回反馈消息
        connectionFactory.isPublisherReturns = true

        // 构建rabbitTemlate操作模板
        val rabbitTemplate = RabbitTemplate(connectionFactory)
        rabbitTemplate.setMandatory(true)
        return rabbitTemplate
    }

    @Autowired
    var env: Environment? = null
    @Bean(name = ["singleListenerContainer"])
    fun listenerContainerFactory(): SimpleRabbitListenerContainerFactory? {
        val containerFactory = SimpleRabbitListenerContainerFactory()
        containerFactory.setConnectionFactory(connectionFactory)
        containerFactory.setMessageConverter(Jackson2JsonMessageConverter())

        // 设置消费者的个数
        containerFactory.setConcurrentConsumers(1)
        // 设置消费者的最大值
        containerFactory.setMaxConcurrentConsumers(1)
        // 设置消费者每次拉取的消息数量，即消费者一次拉取几条消息
        containerFactory.setPrefetchCount(1)

        // 设置确认消息模型为自动确认消费AUTO，目的是防止消息丢失和消息被重复消费
        containerFactory.setAcknowledgeMode(AcknowledgeMode.AUTO)
        return containerFactory
    }
}