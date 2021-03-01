package com.fake.information.sever.demo.Http.Controller.Api

import com.fake.information.sever.demo.Http.Controller.StatusCode
import com.fake.information.sever.demo.Http.Response.Result
import com.fake.information.sever.demo.JWT.JWTManage
import com.fake.information.sever.demo.Model.VerifyTextResult
import com.fake.information.sever.demo.Until.DemoOkhttp
import com.google.gson.GsonBuilder
import org.springframework.scheduling.annotation.Async
import org.springframework.web.bind.annotation.*
import java.net.URL

@RestController
@RequestMapping("/v1/FakeNewsVerify", method = [RequestMethod.POST, RequestMethod.GET])
class FakeNewsTextVerify {
    @PostMapping("/")
    fun postCommitFile(
            @RequestBody text: String,
            @RequestHeader("id") id: Int,
            @RequestHeader("token") token:String
    ): Result<Any> {
        if (JWTManage.verifyToken(token) != JWTManage.TokenVerifyCode.Success.verifyCode) {
            return Result<Any>(
                    success = false,
                    code = StatusCode.Status_401.statusCode,
                    msg = "Token无效"
            )
        }
        val data = DemoOkhttp.Builder()
                .post(text = text,url = "http://127.0.0.1:4336/VerifyFakeNews")
        return Result<Any>(
                success = true,
                code = StatusCode.Status_200.statusCode,
                msg = "success",
                data = data
        )
    }
}