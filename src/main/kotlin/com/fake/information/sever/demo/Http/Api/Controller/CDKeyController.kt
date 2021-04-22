package com.fake.information.sever.demo.Http.Api.Controller

import com.fake.information.sever.demo.DTO.UserRepository
import com.fake.information.sever.demo.Http.Api.Response.Result
import com.fake.information.sever.demo.Http.Api.Response.StatusCode
import com.fake.information.sever.demo.Model.CDKey
import com.fake.information.sever.demo.Redis.FakeNewsRedisTemplate
import com.fake.information.sever.demo.Until.AsyncTask.AsyncService
import com.fake.information.sever.demo.Until.UUID
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpSession

@RestController
@RequestMapping("/v1/cdKey")
class CDKeyController {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var asyncService: AsyncService

    @Autowired
    private lateinit var redisTemplate: FakeNewsRedisTemplate

    @PostMapping("/createToken")
    fun createKey(
        session: HttpSession,
        @RequestBody params: Map<String, Any>
    ): Result<String> {
        if (redisTemplate.getRedis(session.id) != StatusCode.Status200.statusCode) {
            throw IllegalAccessException("您没有权限")
        }
        val userId = redisTemplate.getRedis(session.id+"user").toString().toInt()
        val user = userRepository.getOne(userId.toInt())
        val key = CDKey()
        key.key = UUID.getUuid();
        user.keyList.add(key);
        key.user = user
        asyncService.asyncTask {
            val type = params["type"].toString()
            redisTemplate.setRedis(key.toString(),type)
            redisTemplate.setRedis(key.toString()+"nums",0)
            userRepository.save(user)
        }
        return Result(
                success = true,
                code = StatusCode.Status200.statusCode,
                msg = "success",
                data = key.toString()
        )
    }
}