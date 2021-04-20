package com.fake.information.sever.demo.Http.Api.Controller

import com.fake.information.sever.demo.DTO.UserRepository
import com.fake.information.sever.demo.Http.Api.Response.Result
import com.fake.information.sever.demo.Http.Api.Response.StatusCode
import com.fake.information.sever.demo.Model.CDKey
import com.fake.information.sever.demo.Redis.FakeNewsRedisTemplate
import com.fake.information.sever.demo.Until.AsyncTask.AsyncService
import com.fake.information.sever.demo.Until.UUID
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
            @RequestHeader("id") userId:Int
    ): Result<String> {
        val user = userRepository.getOne(userId)
        val key = CDKey()
        key.key = UUID.getUuid();
        user.keyList.add(key);
        key.user = user
        asyncService.asyncTask {
            redisTemplate.setRedis(key.toString(),0)
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