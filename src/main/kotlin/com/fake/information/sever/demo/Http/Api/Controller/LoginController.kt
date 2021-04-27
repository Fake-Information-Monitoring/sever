package com.fake.information.sever.demo.Http.Api.Controller

import com.fake.information.sever.demo.Controller.tools.Check
import com.fake.information.sever.demo.DTO.UserRepository
import com.fake.information.sever.demo.Config.Redis.FakeNewsRedisTemplate
import com.fake.information.sever.demo.DTO.PersonCertifiedRepository
import com.fake.information.sever.demo.Http.Until.RSA
import com.fake.information.sever.demo.Http.Api.Response.StatusCode
import com.fake.information.sever.demo.Http.Api.Response.Result
import com.fake.information.sever.demo.Until.AsyncTask.AsyncService
import com.fake.information.sever.demo.Until.VerifyCode.VerifyCode
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.lang.IllegalArgumentException
import java.util.*
import java.util.concurrent.TimeUnit
import javax.servlet.http.*

@RestController
@RequestMapping("/v1/login")
@Api("登录管理")
class LoginController {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var redisTemplate: FakeNewsRedisTemplate

    @Autowired
    private lateinit var asyncService: AsyncService

    @GetMapping("/")
    @ApiOperation("没用，别管")
    fun getLogin(): String {

        return "Login!"
    }


    @GetMapping("/getPublicKey")
    @ApiOperation("获取公钥（暂时搁置）")
    fun getPublicKey(
        @RequestHeader("User-Agent") userAgent: String,
        request: HttpServletRequest,
        session: HttpSession
    ): Result<ByteArray?> {
        val key = RSA.getKeyPair()
        session.setAttribute(userAgent, key?.private!!)
        return Result(
            success = true,
            code = StatusCode.Status200.statusCode,
            data = key.public.encoded,
            msg = "success"
        )
    }

    @DeleteMapping("/logout")
    @ApiOperation("注销登录")
    fun deleteLogout(
        request: HttpServletRequest,
        session: HttpSession
    ): Result<String> {
        val Id = redisTemplate.getUserId(session)
        asyncService.asyncTask {
            redisTemplate.remove(session.id)
            session.invalidate()
            val tempUser = userRepository.getOne(Id)
            tempUser.lastActive = Date()
            userRepository.save(tempUser)
        }
        return Result(
            success = true,
            code = StatusCode.Status200.statusCode,
            msg = "success"
        )
    }

    @ObsoleteCoroutinesApi
    @ExperimentalStdlibApi
    @PostMapping("/loginWithEmail")
    @ApiOperation("登录接口")
    fun postLoginWithEmail(
        @RequestBody params: Map<String, Any>,
        request: HttpServletRequest,
        session: HttpSession,
        response: HttpServletResponse
    ): Result<Any> {
        val password = params["password"].toString()
        val verify = params["verifyCode"].toString()
        val account: String = params["account"].toString()
        try {
            checkVerifyCode(session, verify)
            val tempUser = userRepository.findByEmail(account)
            Check.checkAccount(VerifyCode(redisTemplate), session, tempUser, password)
            asyncService.asyncTask {
                request.cookies[0].maxAge = 2592000
                redisTemplate.setRedis(tempUser?.id.toString(), StatusCode.Status200.statusCode)
                redisTemplate.setRedis(session.id, StatusCode.Status200.statusCode)
                tempUser?.id?.let { redisTemplate.setRedis(session.id + "user", it) }
                redisTemplate.setTime(session.id, 2592000, TimeUnit.SECONDS)
                redisTemplate.setTime(tempUser?.id.toString(), 2592000, TimeUnit.SECONDS)
            }
        } catch (e: NumberFormatException) {
            throw NumberFormatException("输入格式非法")
        }
        return Result(
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
    @ApiOperation("获取验证码")
    fun getVerifyCode(session: HttpSession, @PathVariable date: String, response: HttpServletResponse) {
        val captcha = VerifyCode(redisTemplate).createCode(session, "verifyCode")
        captcha.write(response.outputStream)
        response.outputStream.close()
//        return Base64.decode(captcha.imageBase64)
    }
}