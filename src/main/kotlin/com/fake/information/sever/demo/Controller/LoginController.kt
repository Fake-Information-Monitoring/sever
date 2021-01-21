package com.fake.information.sever.demo.Controller.Api

import com.fake.information.sever.demo.Controller.BuildError
import com.fake.information.sever.demo.DAO.UserRepository
import com.fake.information.sever.demo.Model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.servlet.server.Session
import org.springframework.data.domain.Example
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession


@RestController
@RequestMapping("/v1/login", method = [RequestMethod.POST,RequestMethod.GET])
class ProductServiceController {
    @Autowired
    private lateinit var userRepository: UserRepository
    private fun checkEmail(email: String): Boolean {
        return email.matches(Regex("^[a-z0-9A-Z]+[- |a-z0-9A-Z._]+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-z]{2,}$"))
    }
    @ExperimentalStdlibApi
    fun checkAccount(user:User?, password: String): Map<String, String> {
        var status = "failed"
        var error = ""
        when {
            user == null -> {
                error = "用户不存在"
            }
            password!=user.password -> {
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
    @PostMapping("/login_with_email")
    fun loginWithEmail(@RequestParam("account") account: String,
                       @RequestParam("password") password: String,
                       @RequestParam("CAPTCHA") captcha: String
    ): String {
        //TODO:检验验证码是否正确
        var tempUser: User? = null
        if (checkEmail(account)) {
            tempUser = userRepository.findByEmail(account)
        }
        val check = checkAccount(tempUser,password)

        //TODO:添加Session
        return check.toString()
    }

    @ExperimentalStdlibApi
    @PostMapping("/login_with_phone")
    fun loginWithPhone(@RequestParam("account") account: String,
                       @RequestParam("password") password: String,
                       @RequestParam("CAPTCHA") captcha: String,
                       session: HttpSession
    ): String {
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
        return check.toString()
    }

    @PostMapping("/verifyCode")
    fun verifyCode(request: HttpServletRequest, response: HttpServletResponse) {
        //TODO:返回验证码
    }

}