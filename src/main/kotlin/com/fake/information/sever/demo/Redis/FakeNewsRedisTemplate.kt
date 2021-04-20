package com.fake.information.sever.demo.Redis

import com.fake.information.sever.demo.Http.Api.Response.StatusCode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit
import javax.servlet.http.HttpSession

@Service
class FakeNewsRedisTemplate {
    @Autowired
    private lateinit var redisTemplates: RedisTemplate<String, Any>
    fun getUserId(session: HttpSession): Int {
        if (getRedis(session.id) != StatusCode.Status200.statusCode) {
            throw IllegalAccessException("您没有权限")
        }
        return getRedis(session.id+"user").toString().toInt()
    }
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