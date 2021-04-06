package com.fake.information.sever.demo.Controller

import com.fake.information.sever.demo.Controller.tools.Check
import com.fake.information.sever.demo.DTO.UserRepository
import com.fake.information.sever.demo.Redis.FakeNewsRedisTemplate
import com.fake.information.sever.demo.Until.EmailUntil.MailService
import com.fake.information.sever.demo.Http.Response.StatusCode
import com.fake.information.sever.demo.Model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*
import com.fake.information.sever.demo.Http.Response.Result
import com.fake.information.sever.demo.Until.AsyncTask.AsyncService
import com.fake.information.sever.demo.Until.VerifyCode.VerifyCode
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.springframework.scheduling.annotation.Async
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

@RestController
@RequestMapping("v1/signUp", method = [RequestMethod.POST, RequestMethod.GET])
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
    fun getEmail(
            request: HttpServletRequest,
            session: HttpSession,
            @RequestBody params: Map<String, Any>
    ): Result<String> {
        val email = params["email"].toString()
        val verifyCode = VerifyCode(redisTemplate)
                .createCode(session, "emailCode")
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