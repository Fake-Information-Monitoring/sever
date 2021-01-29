package com.fake.information.sever.demo.Controller

import cn.hutool.core.codec.Base64
import com.fake.information.sever.demo.Controller.tools.Check
import com.fake.information.sever.demo.DAO.UserRepository
import com.fake.information.sever.demo.Http.Controller.Api.Until.RSA
import com.fake.information.sever.demo.Http.Controller.StatusCode
import com.fake.information.sever.demo.Http.Response.Result
import com.fake.information.sever.demo.Model.User
import com.fake.information.sever.demo.SessionManager.SessionManage
import com.fake.information.sever.demo.VerifyCode.VerifyCode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/v1/login",
        method = [RequestMethod.POST, RequestMethod.GET, RequestMethod.DELETE])
class LoginController {
    @Autowired
    private lateinit var userRepository: UserRepository

    @GetMapping("/")
    fun getLogin(): String {

        return "Login!"
    }

    @GetMapping("/getPublicKey")
    fun getPublicKey(@RequestHeader("User-Agent") userAgent: String,
                     request: HttpServletRequest): Result<ByteArray?> {
        val key = RSA.getKeyPair()
        SessionManage(request).createSession(userAgent, key?.private!!)
        return Result<ByteArray?>(
                success = true,
                code = StatusCode.Status_200.statusCode,
                data = key.public.encoded,
                msg = "success"
        )
    }

    @DeleteMapping("/logout")
    fun deleteLogout(
            @RequestHeader("id") Id: Int,
            request: HttpServletRequest
    ): Result<String> {
        try {
            SessionManage(request).deleteSession()
            val tempUser = userRepository.getOne(Id)
            tempUser.lastActive = Date()
            userRepository.save(tempUser)
            return Result<String>(
                    success = true,
                    code = StatusCode.Status_200.statusCode,
                    msg = "success"
            )
        } catch (e: Exception) {
            return Result<String>(
                    success = false,
                    code = StatusCode.Status_502.statusCode,
                    msg = e.toString()
            )
        }
    }

    @ExperimentalStdlibApi
    @PostMapping("/loginWithEmail")
    fun postLoginWithEmail(@RequestHeader("account") account: String,
                           @RequestHeader("password") password: String,
                           @RequestHeader("CAPTCHA") captcha: String,
                           @RequestHeader("User-Agent") userAgent: String,
                           request: HttpServletRequest
    ): Result<String> {
        if (!VerifyCode().verifyCode(request, captcha)){
            VerifyCode().createCode(request)
            return Result<String>(
                    success = true,
                    code = StatusCode.Status_401.statusCode,
                    msg = "验证码错误"
            )
        }
//        val privateKey: PrivateKey = SessionController(request).getSessionValue(userAgent) as PrivateKey
//        val passwordWithPrivateKey = RSA.decryptByPrivateKey(password, privateKey)
        val passwordWithPrivateKey = password
        var tempUser: User? = null
        if (Check.checkEmail(account)) {
            tempUser = userRepository.findByEmail(account)
            if (tempUser == null) {
                return Result<String>(
                        success = false,
                        code = StatusCode.Status_401.statusCode,
                        msg = "该用户不存在"
                )
            }
        }
        val check = Check.checkAccount(tempUser, passwordWithPrivateKey)
        if (check.success == true && tempUser != null) {
            SessionManage(request).createSession(tempUser.id.toString(), check.code)
        }
        //TODO:如果第一次密码不正确就生成验证码
        return check
    }

    @ExperimentalStdlibApi
    @PostMapping("/loginWithPhone")
    fun postLoginWithPhone(@RequestHeader("account") account: String,
                           @RequestHeader("password") password: String,
                           @RequestHeader("CAPTCHA") captcha: String,
                           @RequestHeader("User-Agent") userAgent: String,
                           request: HttpServletRequest
    ): Result<String> {
        if (!VerifyCode().verifyCode(request, captcha)){
            VerifyCode().createCode(request)
            return Result<String>(
                    success = true,
                    code = StatusCode.Status_401.statusCode,
                    msg = "验证码错误"
            )
        }
//        val privateKey: PrivateKey = SessionController(request).getSessionValue(userAgent) as PrivateKey
//        val passwordWithPrivateKey = RSA.decryptByPrivateKey(password, privateKey)
        val passwordWithPrivateKey = password
        val tempUser: User?
        try {
            tempUser = userRepository.findByPhoneNumber(account.toLong())
        } catch (e: NumberFormatException) {
            return Result<String>(
                    success = false,
                    code = StatusCode.Status_401.statusCode,
                    msg = "输入格式非法"
            )
        }
        val check = Check.checkAccount(tempUser, passwordWithPrivateKey)
        if (check.success == true) {
            SessionManage(request).createSession(tempUser?.id.toString(), check.code)
        }
        return check
    }

    @GetMapping("/verifyCode", produces = [MediaType.IMAGE_PNG_VALUE, "image/png"])
    fun getVerifyCode(request: HttpServletRequest): ByteArray? {
        return Base64.decode(VerifyCode().createCode(request).imageBase64)
    }
}