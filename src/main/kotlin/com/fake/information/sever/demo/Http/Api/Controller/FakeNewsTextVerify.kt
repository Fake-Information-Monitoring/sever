package com.fake.information.sever.demo.Http.Api.Controller

import cn.hutool.extra.tokenizer.TokenizerException
import com.fake.information.sever.demo.Http.Api.Response.TokenType
import com.fake.information.sever.demo.Http.Until.VerifyResultFactory
import com.fake.information.sever.demo.Model.VerifyBaseModel
import com.fake.information.sever.demo.Redis.FakeNewsRedisTemplate
import com.fake.information.sever.demo.Until.AsyncTask.AsyncService
import com.fake.information.sever.demo.Until.JWT.TokenConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpSession
import javax.websocket.server.PathParam

@RestController
@RequestMapping("/v1/FakeNewsVerify", method = [RequestMethod.POST, RequestMethod.GET])
class FakeNewsTextVerify {
    @Autowired
    private lateinit var redisTemplate: FakeNewsRedisTemplate
    @Autowired
    private lateinit var asyncService: AsyncService

    fun verifyToken(token: String): Boolean {
        val count = redisTemplate.getRedis(token + "nums").toString().toInt()
        redisTemplate.setRedis(token + "nums", count + 1)
        if (count >= TokenConfig.TOKEN_GET_COUNT) {
            return false
        }
        return true
    }

    @PostMapping("/testVerify")
    fun testVerify(
        type: String,
        @RequestBody params: Map<String, Any>,
        session: HttpSession
    ): VerifyBaseModel<*> {
        if (redisTemplate.getRedis(session.id + "TempUUID") == null){
                redisTemplate.setRedis(session.id + "TempUUID", TokenType.TEST.toString())
                redisTemplate.setRedis(session.id + "TempUUID" + "nums", 0)
        }
        val num = redisTemplate.getRedis(session.id + "TempUUID" + "nums").toString().toInt() ?: throw Exception("次数不足")
        if (num < 50) {
            redisTemplate.setRedis(session.id + "TempUUID" + "nums", num + 1)
        }
        val text = params["text"].toString()
        return VerifyResultFactory.getResult(type, text)
    }

    @PostMapping("/")
    fun postVerifyText(
        type: String,
        @RequestBody params: Map<String, Any>,
        @RequestHeader("token") token: String
    ): VerifyBaseModel<*> {
        if (!verifyToken(token)) {
            throw TokenizerException("该UUID已失效,请申请新UUID")
        }
        var requestType = redisTemplate.getRedis(token).toString() ?: throw IllegalArgumentException("无效的UUID")
        if (requestType == TokenType.TEST.toString()) {
            requestType = type
        }
        val text = params["text"].toString()
        return VerifyResultFactory.getResult(requestType, text)
    }
}