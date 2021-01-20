package com.fake.information.sever.demo.Controller.Api

import com.fake.information.sever.demo.DAO.UserRepository
import com.fake.information.sever.demo.Model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Example
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@RestController
@RequestMapping("/v1")
class ProductServiceController{
    @Autowired
    private lateinit var userRepository: UserRepository
    private fun checkEmail(email: String): Boolean {
        return email.matches(Regex("^[a-z0-9A-Z]+[- |a-z0-9A-Z._]+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-z]{2,}$"))
    }


    @ExperimentalStdlibApi
    @PostMapping("/login")
    fun login(@RequestParam("account") account: String,
              @RequestParam("password") password: String,
              @RequestParam("CAPTCHA") captcha: String
    ): String {
            val user = User()
            if (checkEmail(account)) {
                user.email = account
            }else {
                try {
                    user.phoneNumber = account.toLong()
                }catch (e:NumberFormatException){
                    return e.toString()
                }
            }
            val example = Example.of(user)
            val tempUser = userRepository.findOne(example)
            if (tempUser.isEmpty){
                return buildMap<String,String> {
                    "error" to "用户不存在"
                }.toString()
            }
            return "success"
    }

    @PostMapping("/verifyCode")
    fun verifyCode(request: HttpServletRequest, response: HttpServletResponse){
            //TODO:返回验证码
    }

}