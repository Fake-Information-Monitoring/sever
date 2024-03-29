package com.fake.information.sever.demo.Http.Api.Controller

import com.fake.information.sever.demo.Controller.tools.Check
import com.fake.information.sever.demo.DAO.UserRepository
import com.fake.information.sever.demo.Config.Redis.FakeNewsRedisTemplate
import com.fake.information.sever.demo.Until.EmailUntil.MailService
import com.fake.information.sever.demo.Http.Api.Response.StatusCode
import com.fake.information.sever.demo.Model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*
import com.fake.information.sever.demo.Http.Api.Response.Result
import com.fake.information.sever.demo.Until.AsyncTask.AsyncService
import com.fake.information.sever.demo.Until.VerifyCode.VerifyCode
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kotlinx.coroutines.ObsoleteCoroutinesApi
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

@RestController
@RequestMapping("v1/signUp", method = [RequestMethod.POST, RequestMethod.GET])
@Api("注册管理")
class SignUpController {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var mailService: MailService

    @Autowired
    private lateinit var redisTemplate: FakeNewsRedisTemplate
    
    @Autowired
    private lateinit var asyncService: AsyncService

    @ObsoleteCoroutinesApi
    @PostMapping("/email")
    @ApiOperation("发送邮件")
    fun getEmail(
            request: HttpServletRequest,
            session: HttpSession,
            @RequestBody params: Map<String, Any>
    ): Result<String> {
        val email = params["email"].toString()
        val verifyCode = VerifyCode(redisTemplate)
                .createCode(session, "emailCode")
        if(!Check.checkEmail(email))
            throw IllegalArgumentException("邮箱格式有误")
        asyncService.asyncTask {
            mailService.sendSimpleMail(email, "验证码,五分钟内有效", verifyCode.code)
        }
        return Result<String>(
                success = true,
                code = StatusCode.Status200.statusCode,
                msg = "success"
        )
    }

    @ExperimentalStdlibApi
    @PostMapping("/create")
    @ApiOperation("注册")
    fun postCreate(
            session: HttpSession,
            request: HttpServletRequest,
            @RequestBody params:Map<String,Any>
    ): Result<String> {
        val email = params["email"].toString()
        val password = params["password"].toString()
        val phoneNumber = params["phoneNumber"].toString()
        val name = params["name"].toString()
        val verifyCode = params["emailCode"].toString()
        val thisName = name
        Check.checking(email, password, thisName, userRepository)
        if (!VerifyCode(redisTemplate)
                        .verifyCode(session, verifyCode, "emailCode")
        ) {
            throw IllegalArgumentException("验证码错误")
        }
        val user = User()
        user.email = email
        user.phoneNumber = phoneNumber
        user.setPassword(password)
        user.name = thisName
        user.update = Date()
        asyncService.asyncTask {
            userRepository.save(user)
        }
        return Result<String>(
                success = true,
                code = StatusCode.Status200.statusCode,
                msg = "success"
        )
    }
}