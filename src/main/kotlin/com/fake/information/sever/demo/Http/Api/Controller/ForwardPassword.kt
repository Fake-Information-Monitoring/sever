package com.fake.information.sever.demo.Http.Api.Controller

import com.fake.information.sever.demo.Controller.tools.Check
import com.fake.information.sever.demo.DTO.UserRepository
import com.fake.information.sever.demo.Config.Redis.FakeNewsRedisTemplate
import com.fake.information.sever.demo.Until.EmailUntil.MailService
import com.fake.information.sever.demo.Http.Api.Response.StatusCode
import com.fake.information.sever.demo.Http.Api.Response.Result
import com.fake.information.sever.demo.Until.AsyncTask.AsyncService
import com.fake.information.sever.demo.Until.VerifyCode.VerifyCode
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

@RestController
@RequestMapping("/v1/forward")
@Api("修改密码")
class ForwardPassword {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var mailService: MailService

    @Autowired
    private lateinit var redisTemplate: FakeNewsRedisTemplate

    @Autowired
    private lateinit var asyncService: AsyncService

    private lateinit var verifyCode: VerifyCode


    @GetMapping("/")
    @ApiOperation("验证邮件验证码是否正确")
    fun forwardPassword(
        session: HttpSession,
        @RequestHeader("verifyCode") verify: String,
        response:HttpServletResponse
    ): Result<String> {
        if (verifyCode == null) {
            verifyCode = VerifyCode(redisTemplate)
        }
        if (!verifyCode.verifyCode(session, verify, "emailCode")) {
            response.status = StatusCode.Status401.statusCode
            return Result<String>(
                success = false,
                code = StatusCode.Status401.statusCode,
                msg = "验证码错误"
            )
        }
        redisTemplate.setRedis(session.id + "forward", true)
        return Result(
            success = true,
            code = StatusCode.Status200.statusCode,
            msg = "success"
        )
    }

    @PutMapping("/change")
    @ApiOperation("修改密码")
    fun changePassword(
        session: HttpSession,
        @RequestParam params: Map<String, Any>,
        response:HttpServletResponse
    ): Result<String> {
        val email = params["email"].toString()
        val password = params["changePassword"].toString()
        if (redisTemplate.getRedis(session.id + "forward") != true) {
            response.status = StatusCode.Status401.statusCode
            return Result<String>(
                success = false,
                code = StatusCode.Status401.statusCode,
                msg = "未通过验证！"
            )
        }
        if (!Check.checkPassword(password)) {
            response.status = StatusCode.Status401.statusCode
            return Result(
                success = false,
                code = StatusCode.Status401.statusCode,
                msg = "密码不合法！"
            )
        }
        val user = userRepository.findByEmail(email)
        user!!.setPassword(password)
        asyncService.asyncTask {
            userRepository.save(user)
        }
        return Result(
            success = true,
            code = StatusCode.Status200.statusCode,
            msg = "success"
        )
    }

    @ObsoleteCoroutinesApi
    @GetMapping("/sendEmail")
    @ApiOperation("发送验证邮件")
    fun forwardPasswordCheck(
        @RequestHeader("email") email: String,
        session: HttpSession
    ): Result<String> {
        if (verifyCode == null) {
            verifyCode = VerifyCode(redisTemplate)
        }
        if (!Check.checkEmail(email)) throw Exception("邮箱格式有错")
        val verifyCodes = verifyCode.createCode(session, "emailCode")
        asyncService.asyncTask {
            mailService.sendSimpleMail(email, "您正在修改密码，验证码,五分钟内有效", verifyCodes.code)
        }
        return Result(
            success = true,
            code = StatusCode.Status200.statusCode,
            msg = "success"
        )
    }

}