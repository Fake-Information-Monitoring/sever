package com.fake.information.sever.demo.Http.Controller

import com.fake.information.sever.demo.DTO.CDKeyRepository
import com.fake.information.sever.demo.Http.Response.StatusCode
import com.fake.information.sever.demo.Http.Response.Result
import com.fake.information.sever.demo.Model.VerifyTextResult
import com.fake.information.sever.demo.Redis.FakeNewsRedisTemplate
import com.fake.information.sever.demo.Until.JWT.TokenConfig
import com.fake.information.sever.demo.Until.JWT.VerifyToken
import com.fake.information.sever.demo.Until.Requests.DemoOkhttp
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/FakeNewsVerify", method = [RequestMethod.POST, RequestMethod.GET])
class FakeNewsTextVerify {
    @Autowired
    private lateinit var redisTemplate: FakeNewsRedisTemplate

    @Autowired
    private lateinit var cdKeyRepository: CDKeyRepository


    fun verifyToken(token: String): Boolean {
        val key = VerifyToken().verifyJwt(token)?.body
        val keyModel = cdKeyRepository.findById(key?.get("keyId").toString().toInt()).get()
        val count: Int = redisTemplate.getRedis(
            keyModel.id.toString()
        ).toString().toInt() ?: 1
        redisTemplate.setRedis(keyModel.id.toString(), count)
        if (count >= TokenConfig.TOKEN_GET_COUNT) {
            return false
        }
        return true
    }

    @PostMapping("/")
    fun postCommitFile(
        @RequestBody text: String,
        @RequestHeader("id") id: Int,
        @RequestHeader("token") token: String
    ): Result<Any> {
        if (!verifyToken(token)) {
            return Result<Any>(
                success = false,
                code = StatusCode.Status401.statusCode,
                msg = "Token无效"
            )
        }
        val data = DemoOkhttp.post<VerifyTextResult>(text = text,
            url = "http://127.0.0.1:4336/VerifyFakeNews")
        return Result<Any>(
            success = true,
            code = StatusCode.Status200.statusCode,
            msg = "success",
            data = data
        )
    }
}