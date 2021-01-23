package com.fake.information.sever.demo.Controller

import com.fake.information.sever.demo.Controller.tools.Check
import com.fake.information.sever.demo.DAO.UserRepository
import com.fake.information.sever.demo.Http.Controller.SessionController
import com.fake.information.sever.demo.Http.Controller.StatusCode
import com.fake.information.sever.demo.Http.Response.Result
import com.fake.information.sever.demo.Model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/v1/login", method = [RequestMethod.POST, RequestMethod.GET])
class LoginController {
    @Autowired
    private lateinit var userRepository: UserRepository

    @GetMapping("/")
    fun login(): String {

        return "Login!"
    }

    @PostMapping("/logout")
    fun logout(
            @RequestHeader("id") Id: Int,
            request: HttpServletRequest
    ): Result<String> {
        try {
            SessionController(request).deleteSession()
            return Result<String>(
                    success = true,
                    code = StatusCode.Status_200.statusCode,
                    msg = "success"
            )
        }catch (e:Exception){
            return Result<String>(
                    success = false,
                    code = StatusCode.Status_502.statusCode,
                    msg = e.toString()
            )
        }
    }

    @ExperimentalStdlibApi
    @PostMapping("/loginWithEmail")
    fun loginWithEmail(@RequestHeader("account") account: String,
                       @RequestHeader("password") password: String,
                       @RequestHeader("CAPTCHA") captcha: String,
                       request:HttpServletRequest
    ): String {
        var tempUser: User? = null
        if (Check.checkEmail(account)) {
            tempUser = userRepository.findByEmail(account)
            if (tempUser == null){
                return Result<String>(
                        success = false,
                        code = StatusCode.Status_401.statusCode,
                        msg = "该用户不存在"
                ).toJson()
            }
        }
        val check = Check.checkAccount(tempUser, password)
        if (check.success == true && tempUser !=null){
                SessionController(request).createSession(tempUser.id.toString(), check.code)
        }
        //TODO:如果第一次密码不正确就生成验证码
        return check.toJson()
    }

    @ExperimentalStdlibApi
    @PostMapping("/loginWithPhone")
    fun loginWithPhone(@RequestHeader("account") account: String,
                       @RequestHeader("password") password: String,
                       @RequestHeader("CAPTCHA") captcha: String,
                       request:HttpServletRequest
    ): String {
        //TODO:检验验证码是否正确
         val tempUser: User?
        try {
            tempUser = userRepository.findByPhoneNumber(account.toLong())
        } catch (e: NumberFormatException) {
            return Result<String>(
                    success = false,
                    code = StatusCode.Status_401.statusCode,
                    msg = "输入格式非法"
            ).toJson()
        }
        val check = Check.checkAccount(tempUser, password)
        if (check.success == true){
            SessionController(request).createSession(tempUser?.id.toString(), check.code)
        }
        return check.toJson()
    }

    @PostMapping("/verifyCode")
    fun verifyCode() {
    }

}