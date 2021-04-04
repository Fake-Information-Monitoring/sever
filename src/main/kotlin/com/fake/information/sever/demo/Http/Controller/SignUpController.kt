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
import com.fake.information.sever.demo.Until.AsyncTask.FakeNewsAsyncService
import com.fake.information.sever.demo.Until.VerifyCode.VerifyCode
import kotlinx.coroutines.ObsoleteCoroutinesApi
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

    @ObsoleteCoroutinesApi
    @PostMapping("/email")
    fun getEmail(
            request: HttpServletRequest,
            session: HttpSession,
            @RequestHeader("email") email: String
    ): Result<String> {
        val verifyCode = VerifyCode(redisTemplate)
                .createCode(session, "emailCode")
        FakeNewsAsyncService().asyncTask {
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
            @RequestParam params:Map<String,Any>
    ): Result<String> {
        val email = params["email"].toString()
        val password = params["password"].toString()
        val phoneNumber = params["phoneNumber"].toString()
        val sex = params["sex"].toString()
        val name = params["name"].toString()
        val verifyCode = params["emailCode"].toString()
        val thisName = Check.encode(name)
        val thisSex = Check.encode(sex)
        Check.checking(email, password, thisSex, thisName, userRepository)
        if (!VerifyCode(redisTemplate)
                        .verifyCode(session, verifyCode, "emailCode")
        ) {
            throw IllegalArgumentException("验证码错误")
        }
        FakeNewsAsyncService().asyncTask {
            val user = User()
            user.email = email
            user.phoneNumber = phoneNumber.toLong()
            user.setPassword(password)
            user.name = thisName
            user.update = Date()
            userRepository.save(user)
        }
        return Result<String>(
                success = true,
                code = StatusCode.Status200.statusCode,
                msg = "success"
        )
    }
}