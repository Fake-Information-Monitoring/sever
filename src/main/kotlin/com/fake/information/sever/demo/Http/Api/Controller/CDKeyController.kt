package com.fake.information.sever.demo.Http.Api.Controller

import com.fake.information.sever.demo.DTO.UserRepository
import com.fake.information.sever.demo.Http.Api.Response.Result
import com.fake.information.sever.demo.Http.Api.Response.StatusCode
import com.fake.information.sever.demo.Http.Api.Response.TokenType
import com.fake.information.sever.demo.Model.CDKey
import com.fake.information.sever.demo.Config.Redis.FakeNewsRedisTemplate
import com.fake.information.sever.demo.Until.AsyncTask.AsyncService
import com.fake.information.sever.demo.Until.UUID.UUID
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.hamcrest.text.IsEmptyString
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.HttpSession

@Api(value = "Token-UUID信息管理")
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
    @ApiOperation(value = "当用户通过认证以后创建一个UUID")
    fun createKey(
        session: HttpSession,
        @RequestBody params: Map<String, Any>
    ): Result<String> {
        if (redisTemplate.getRedis(session.id) != StatusCode.Status200.statusCode) {
            throw IllegalAccessException("您没有权限")
        }
        val userId = redisTemplate.getRedis(session.id + "user").toString().toInt()
        val user = userRepository.getOne(userId.toInt())
        if (user.personCertified == null){
            throw IllegalAccessException("请先进行认证")
        }
        val key = CDKey()
        key.key = UUID.getUuid();
        user.keyList.add(key);
        key.user = user
        val name = params["name"].toString()
        val type = params["type"].toString()
        key.name = name
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