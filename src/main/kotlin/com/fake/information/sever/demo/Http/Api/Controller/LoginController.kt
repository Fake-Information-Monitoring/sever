package com.fake.information.sever.demo.Controller

import cn.hutool.core.codec.Base64
import com.fake.information.sever.demo.Controller.tools.Check
import com.fake.information.sever.demo.DTO.UserRepository
import com.fake.information.sever.demo.Redis.FakeNewsRedisTemplate
import com.fake.information.sever.demo.Http.Until.RSA
import com.fake.information.sever.demo.Http.Api.Response.StatusCode
import com.fake.information.sever.demo.Http.Api.Response.Result
import com.fake.information.sever.demo.Until.AsyncTask.AsyncService
import com.fake.information.sever.demo.Until.VerifyCode.VerifyCode
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.lang.IllegalArgumentException
import java.util.*
import java.util.concurrent.TimeUnit
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

@RestController
@RequestMapping(
    "/v1/login",
    method = [RequestMethod.POST, RequestMethod.GET, RequestMethod.DELETE]
)
class LoginController {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var redisTemplate: FakeNewsRedisTemplate

    @Autowired
    private lateinit var asyncService: AsyncService

    @GetMapping("/")
    fun getLogin(): String {

        return "Login!"
    }


    @GetMapping("/getPublicKey")
    fun getPublicKey(
        @RequestHeader("User-Agent") userAgent: String,
        request: HttpServletRequest,
        session: HttpSession
    ): Result<ByteArray?> {
        val key = RSA.getKeyPair()
        session.setAttribute(userAgent, key?.private!!)
        return Result<ByteArray?>(
            success = true,
            code = StatusCode.Status200.statusCode,
            data = key.public.encoded,
            msg = "success"
        )
    }

    @DeleteMapping("/logout")
    fun deleteLogout(
        @RequestHeader("id") Id: Int,
        request: HttpServletRequest,
        session: HttpSession
    ): Result<String> {
        asyncService.asyncTask {
            redisTemplate.remove(session.id)
            session.invalidate()
            val tempUser = userRepository.getOne(Id)
            tempUser.lastActive = Date()
            userRepository.save(tempUser)
        }
        return Result<String>(
            success = true,
            code = StatusCode.Status200.statusCode,
            msg = "success"
        )
    }

    @ObsoleteCoroutinesApi
    @ExperimentalStdlibApi
    @PostMapping("/loginWithEmail")
    fun postLoginWithEmail(
        @RequestBody params: Map<String, Any>,
        request: HttpServletRequest,
        session: HttpSession
    ): Result<Any> {
        val password = params["password"].toString()
        val verify = params["verifyCode"].toString()
        val account: String = params["account"].toString()
        try {
            checkVerifyCode(session, verify)
            val tempUser = userRepository.findByEmail(account)
            Check.checkAccount(VerifyCode(redisTemplate), session, tempUser, password)
            asyncService.asyncTask {
                redisTemplate.setRedis(tempUser?.id.toString(), StatusCode.Status200.statusCode)
                redisTemplate.setRedis(session.id, StatusCode.Status200.statusCode)
                tempUser?.id?.let { redisTemplate.setRedis(session.id + "user", it) }
                redisTemplate.setTime(session.id, 1000 * 60 * 60 * 24 * 7, TimeUnit.SECONDS)
                redisTemplate.setTime(tempUser?.id.toString(), 1000 * 60 * 60 * 24 * 7, TimeUnit.SECONDS)
            }
        } catch (e: NumberFormatException) {
            throw NumberFormatException("输入格式非法")
        }

        return Result<Any>(
            success = true,
            code = StatusCode.Status200.statusCode,
            msg = "login success",
            data = request.cookies
        )
    }

    @ObsoleteCoroutinesApi
    fun checkVerifyCode(session: HttpSession, verify: String): Boolean {
        val verifyCode = VerifyCode(redisTemplate)
        if (!verifyCode.verifyCode(session, verify, "verifyCode")) {
            verifyCode.createCode(session, "verifyCode")
            throw IllegalArgumentException("验证码错误")
        }
        return true
    }

    @ObsoleteCoroutinesApi
    @GetMapping("/verifyCode/{date}", produces = [MediaType.IMAGE_PNG_VALUE, "image/png"])
    fun getVerifyCode(session: HttpSession, @PathVariable date: String, response: HttpServletResponse) {
        val captcha = VerifyCode(redisTemplate).createCode(session, "verifyCode")
        captcha.write(response.outputStream)
//        return Base64.decode(captcha.imageBase64)
    }
}