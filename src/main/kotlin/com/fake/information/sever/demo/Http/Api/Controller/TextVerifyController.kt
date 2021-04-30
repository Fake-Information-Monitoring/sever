package com.fake.information.sever.demo.Http.Api.Controller

import cn.hutool.extra.tokenizer.TokenizerException
import com.fake.information.sever.demo.Http.Api.Response.TokenType
import com.fake.information.sever.demo.Http.Until.VerifyResultFactory
import com.fake.information.sever.demo.Model.VerifyBaseModel
import com.fake.information.sever.demo.Config.Redis.FakeNewsRedisTemplate
import com.fake.information.sever.demo.Until.AsyncTask.AsyncService
import com.fake.information.sever.demo.Until.JWT.TokenConfig
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpSession

@RestController
@RequestMapping("/v1/FakeNewsVerify")
@Api("AI服务管理")
class TextVerifyController {
    @Autowired
    private lateinit var redisTemplate: FakeNewsRedisTemplate

    fun verifyToken(token: String): Boolean {
        val count = redisTemplate.getRedis(token + "nums").toString().toInt()
        redisTemplate.setRedis(token + "nums", count + 1)
        if (count >= TokenConfig.TOKEN_GET_COUNT) {
            return false
        }
        return true
    }

    @PostMapping("/testVerify")
    @ApiOperation("测试监测接口，用于向用户首次展示产品")
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
    @ApiOperation("AI服务接口，根据UUID判别服务类型")
    fun postVerifyText(
        type: String,
        @RequestBody params: Map<String, Any>,
        @RequestHeader("token") token: String
    ): VerifyBaseModel<*> {
        if (!verifyToken(token)) {
            throw TokenizerException("该UUID已失效,请申请新UUID")
        }
        var requestType = redisTemplate.getRedis(token+"type").toString() ?: throw IllegalArgumentException("无效的UUID")
        if (requestType == TokenType.TEST.toString()) {
            requestType = type
        }
        val text = params["text"].toString()
        return VerifyResultFactory.getResult(requestType, text,token)
    }
}