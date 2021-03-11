package com.fake.information.sever.demo.Controller

import cn.hutool.core.codec.Base64
import com.fake.information.sever.demo.Controller.tools.Check
import com.fake.information.sever.demo.DAO.UserRepository
import com.fake.information.sever.demo.DAO.Redis.FakeNewsRedisTemplate
import com.fake.information.sever.demo.Http.Controller.Api.Until.RSA
import com.fake.information.sever.demo.Http.Controller.StatusCode
import com.fake.information.sever.demo.Http.Response.Result
import com.fake.information.sever.demo.VerifyCode.VerifyCode
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.lang.IllegalArgumentException
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

@RestController
@RequestMapping("/v1/login",
        method = [RequestMethod.POST, RequestMethod.GET, RequestMethod.DELETE])
class LoginController {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var redisTemplate: FakeNewsRedisTemplate


    @GetMapping("/")
    fun getLogin(): String {

        return "Login!"
    }

    @GetMapping("/getPublicKey")
    fun getPublicKey(@RequestHeader("User-Agent") userAgent: String,
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
        redisTemplate.remove(session.id)
        session.invalidate()
        val tempUser = userRepository.getOne(Id)
        tempUser.lastActive = Date()
        userRepository.save(tempUser)
        return Result<String>(
                success = true,
                code = StatusCode.Status200.statusCode,
                msg = "success"
        )
    }

    @ObsoleteCoroutinesApi
    @ExperimentalStdlibApi
    @PostMapping("/loginWithEmail")
    fun postLoginWithEmail(@RequestBody params: Map<String, Any>,
                           request: HttpServletRequest,
                           session: HttpSession
    ): Result<Any> {
        val password = params["password"].toString()
        val verify = params["verifyCode"].toString()
        val account: String = params["account"].toString()
        try {
            checkVerifyCode(session, verify)
            val tempUser = userRepository.findByPhoneNumber(account.toLong())
            Check.checkAccount(VerifyCode(redisTemplate), session, tempUser, password)
        } catch (e: NumberFormatException) {
            throw NumberFormatException("输入格式非法")
        }
        redisTemplate.setRedis(session.id, StatusCode.Status200.statusCode)
        return Result<Any>(
                success = true,
                code = StatusCode.Status200.statusCode,
                msg = "login success"
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
    @ExperimentalStdlibApi
    @PostMapping("/loginWithPhone")
    fun postLoginWithPhone(@RequestBody params: Map<String, Any>,
                           request: HttpServletRequest,
                           session: HttpSession
    ): Result<Any> {
        val password = params["password"].toString()
        val verify = params["verifyCode"].toString()
        val account: String = params["account"].toString()
//        val privateKey: PrivateKey = session.getAttribute(userAgent) as PrivateKey
//        val passwordWithPrivateKey = RSA.decryptByPrivateKey(password, privateKey)
        try {
            checkVerifyCode(session, verify)
            val tempUser = userRepository.findByPhoneNumber(account.toLong())
            Check.checkAccount(VerifyCode(redisTemplate), session, tempUser, password)
        } catch (e: NumberFormatException) {
            return Result<Any>(
                    success = false,
                    code = StatusCode.Status401.statusCode,
                    msg = "输入格式非法"
            )
        }
        redisTemplate.setRedis(session.id, StatusCode.Status200.statusCode)
        return Result<Any>(
                success = true,
                code = StatusCode.Status200.statusCode,
                msg = "login success"
        )
    }

    @ObsoleteCoroutinesApi
    @GetMapping("/verifyCode/{date}", produces = [MediaType.IMAGE_PNG_VALUE, "image/png"])
    fun getVerifyCode(session: HttpSession, @PathVariable date: String): ByteArray? {
        return Base64.decode(VerifyCode(redisTemplate).createCode(session, "verifyCode").imageBase64)
    }
}