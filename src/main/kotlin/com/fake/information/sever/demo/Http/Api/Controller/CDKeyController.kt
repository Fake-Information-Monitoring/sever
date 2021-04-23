package com.fake.information.sever.demo.Http.Api.Controller

import com.fake.information.sever.demo.DTO.UserRepository
import com.fake.information.sever.demo.Http.Api.Response.Result
import com.fake.information.sever.demo.Http.Api.Response.StatusCode
import com.fake.information.sever.demo.Http.Api.Response.TokenType
import com.fake.information.sever.demo.Model.CDKey
import com.fake.information.sever.demo.Redis.FakeNewsRedisTemplate
import com.fake.information.sever.demo.Until.AsyncTask.AsyncService
import com.fake.information.sever.demo.Until.UUID.UUID
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
    @PostMapping("/testToken")
    fun testToken(session: HttpSession): Result<String> {
        val key = CDKey()
        key.key = UUID.getUuid()
        asyncService.asyncTask {
            redisTemplate.setRedis(key.toString(), TokenType.TEST.toString())
            redisTemplate.setRedis(key.toString() + "nums", 49995)
        }
        return Result(
            success = true,
            code = StatusCode.Status200.statusCode,
            msg = "临时UUID，有五次调用次数，请适当使用",
            data = key.toString()
        )
    }
    @PostMapping("/createToken")
    fun createKey(
        session: HttpSession,
        @RequestBody params: Map<String, Any>
    ): Result<String> {
        if (redisTemplate.getRedis(session.id) != StatusCode.Status200.statusCode) {
            throw IllegalAccessException("您没有权限")
        }
        val userId = redisTemplate.getRedis(session.id + "user").toString().toInt()
        val user = userRepository.getOne(userId.toInt())
        val key = CDKey()
        key.key = UUID.getUuid();
        user.keyList.add(key);
        key.user = user
        val type = params["type"].toString()
        if (!TokenType.hasValue(type)) {
            throw IllegalAccessException("不存在该类别")
        }
        asyncService.asyncTask {
            redisTemplate.setRedis(key.toString(), type)
            redisTemplate.setRedis(key.toString() + "nums", 0)
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