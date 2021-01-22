package com.fake.information.sever.demo.Controller

import com.fake.information.sever.demo.Controller.tools.BuildError
import com.fake.information.sever.demo.Controller.tools.Check
import com.fake.information.sever.demo.DAO.UserRepository
import com.fake.information.sever.demo.Model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession


@RestController
@RequestMapping("/api/v1/login", method = [RequestMethod.POST,RequestMethod.GET])
class ProductServiceController {
    @GetMapping("/")
    fun login(): String {
        return "Login!"
    }
    @Autowired
    private lateinit var userRepository: UserRepository

    @ExperimentalStdlibApi
    fun checkAccount(user:User?, password: String): Map<String, String> {
        var status = "failed"
        var error = ""
        when {
            user == null -> {
                error = "用户不存在"
            }
            password!=user.password -> {
                //TODO：生成验证码并加入Session
                error = "密码错误"

            }
            else -> {
                status = "success"
            }
        }
        return buildMap<String,String> {
            "error" to error
            "status" to status
        }
    }

    @ExperimentalStdlibApi
    @PostMapping("/loginWithEmail")
    fun loginWithEmail(@RequestParam("account") account: String,
                       @RequestParam("password") password: String,
                       @RequestParam("CAPTCHA") captcha: String = ""
    ): Map<String, Any> {
        //TODO:检验验证码是否正确
        var tempUser: User? = null
        if (Check.checkEmail(account)) {
            tempUser = userRepository.findByEmail(account)
        }
        val check = checkAccount(tempUser,password)

        //TODO:添加Session
        return check
    }

    @ExperimentalStdlibApi
    @PostMapping("/loginWithPhone")
    fun loginWithPhone(@RequestParam("account") account: String,
                       @RequestParam("password") password: String,
                       @RequestParam("CAPTCHA") captcha: String = "",
                       session: HttpSession
    ): Map<String, Any> {
        //TODO:检验验证码是否正确
        var tempUser: User? = null
        try {
            tempUser = userRepository.findByPhoneNumber(account.toLong())
        } catch (e: NumberFormatException) {
            return BuildError.buildErrorInfo("账号格式有误")
        }
        val check = checkAccount(tempUser,password)
//        session.setAttribute()
        //TODO:添加Session
        return check
    }

    @PostMapping("/verifyCode")
    fun verifyCode(request: HttpServletRequest, response: HttpServletResponse) {
    }

}