package com.fake.information.sever.demo.DAO.redis

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class FakeNewsRedisTemplate {
    @Autowired
    private lateinit var redisTemplates: RedisTemplate<String, Any>

    fun setRedis(key:String,value:Any) {
        redisTemplates.opsForValue().set(key,value)
    }

    fun setTime(key:String,time:Long,type:TimeUnit){
        redisTemplates.expire(key,time,type)
    }

    fun remove(vararg key: String){
        for(i:String in key){
            if (hasKey(i)){
                redisTemplates.delete(i)
            }
        }
    }

    fun hasKey(key:String): Boolean {
        return redisTemplates.hasKey(key)
    }

    fun getRedis(key:String): Any? {
        return redisTemplates.opsForValue().get(key)
    }
}