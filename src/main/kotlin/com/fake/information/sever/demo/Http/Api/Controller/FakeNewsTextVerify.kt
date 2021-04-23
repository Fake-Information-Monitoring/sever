package com.fake.information.sever.demo.Http.Api.Controller

import cn.hutool.extra.tokenizer.TokenizerException
import com.fake.information.sever.demo.Http.Until.VerifyResultFactory
import com.fake.information.sever.demo.Model.VerifyBaseModel
import com.fake.information.sever.demo.Redis.FakeNewsRedisTemplate
import com.fake.information.sever.demo.Until.JWT.TokenConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/FakeNewsVerify", method = [RequestMethod.POST, RequestMethod.GET])
class FakeNewsTextVerify {
    @Autowired
    private lateinit var redisTemplate: FakeNewsRedisTemplate


    fun verifyToken(token: String): Boolean {
        val count = redisTemplate.getRedis(token+"nums").toString().toInt()
        redisTemplate.setRedis(token+"nums", count + 1)
        if (count >= TokenConfig.TOKEN_GET_COUNT) {
            return false
        }
        return true
    }

    @PostMapping("/")
    fun postCommitFile(
        @RequestBody params: Map<String, Any>,
        @RequestHeader("token") token: String
    ): VerifyBaseModel<*> {
        if (!verifyToken(token)) {
            throw TokenizerException("该UUID已失效")
        }
        val type = redisTemplate.getRedis(token).toString() ?: throw IllegalArgumentException("无效的UUID")
        val text = params["text"].toString()
        return VerifyResultFactory.getResult(type, text)
    }
}