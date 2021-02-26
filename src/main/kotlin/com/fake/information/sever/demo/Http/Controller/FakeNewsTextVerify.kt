package com.fake.information.sever.demo.Http.Controller

import com.fake.information.sever.demo.Http.Response.Result
import com.fake.information.sever.demo.JWT.JWTManage
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/v1/FakeNewsVerify", method = [RequestMethod.POST, RequestMethod.GET])
class FakeNewsTextVerify {
    @PostMapping("/uploadFile")
    fun postCommitFile(
            @RequestHeader text: String,
            @RequestHeader("id") id: Int,
            @RequestHeader("token") token:String
    ): Result<String> {
        if (JWTManage.verifyToken(token) != JWTManage.TokenVerifyCode.Success.verifyCode) {
            return Result<String>(
                    success = false,
                    code = StatusCode.Status_401.statusCode,
                    msg = "Token无效"
            )
        }
        return Result<String>(
                success = true,
                code = StatusCode.Status_200.statusCode,
                msg ="Token无效"
        )
    }
}