package com.fake.information.sever.demo.DAO.redis

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component

@Component
class FakeNewsRedis {
    private lateinit var stringTemplates:StringRedisTemplate

    @Autowired
    fun fakeNewsBean(template: StringRedisTemplate){
        this.stringTemplates = template
    }

}