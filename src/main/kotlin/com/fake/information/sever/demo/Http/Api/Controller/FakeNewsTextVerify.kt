package com.fake.information.sever.demo.Http.Api.Controller

import com.fake.information.sever.demo.Http.Api.Response.Result
import com.fake.information.sever.demo.Http.Api.Response.StatusCode
import com.fake.information.sever.demo.Http.Until.AISeverURL
import com.fake.information.sever.demo.Http.Until.VerifyResultFactory
import com.fake.information.sever.demo.Model.VerifyModel.VerifyBaseModel
import com.fake.information.sever.demo.Redis.FakeNewsRedisTemplate
import com.fake.information.sever.demo.Until.JWT.TokenConfig
import com.fake.information.sever.demo.Until.Requests.DemoOkhttp
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/FakeNewsVerify", method = [RequestMethod.POST, RequestMethod.GET])
class FakeNewsTextVerify {
    @Autowired
    private lateinit var redisTemplate: FakeNewsRedisTemplate


    fun verifyToken(token: String): Boolean {
        val count = redisTemplate.getRedis(token+"nums").toString().toInt()
        redisTemplate.setRedis(token, count + 1)
        if (count >= TokenConfig.TOKEN_GET_COUNT) {
            return false
        }
        return true
    }

    @PostMapping("/")
    fun postCommitFile(
        @RequestBody params: Map<String,Any>,
        @RequestHeader("token") token: String
    ): Result<Any> {
        if (!verifyToken(token)) {
            return Result<Any>(
                success = false,
                code = StatusCode.Status401.statusCode,
                msg = "已失效"
            )
        }
        val type = redisTemplate.getRedis(token).toString()?:throw IllegalArgumentException("无效的UUID")
        val text = params["text"].toString()
//        val resultType = VerifyResultFactory.getResultClass(type)
        val data = DemoOkhttp.post<VerifyBaseModel>(text = text,
            url = AISeverURL.RUMOR_URL.toString())

        return Result<Any>(
            success = true,
            code = StatusCode.Status200.statusCode,
            msg = "success",
            data = data
        )
    }
}