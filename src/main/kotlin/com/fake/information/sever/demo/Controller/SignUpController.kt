package com.fake.information.sever.demo.Controller

import com.fake.information.sever.demo.DAO.UserRepository
import com.fake.information.sever.demo.Model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.net.URLDecoder
import java.util.*

@RestController
@RequestMapping("/v1/sign_up", method = [RequestMethod.POST, RequestMethod.GET])
class SignUpController {
    @Autowired
    private lateinit var userRepository: UserRepository
    fun checkUserByName(name: String): Boolean {
        return userRepository.findByName(name).name != null
    }
    fun encode(str: String): String {
        return URLDecoder.decode(str,"utf-8")
    }
    @PostMapping("/update")
    fun update(){

    }
    @ExperimentalStdlibApi
    @PostMapping("/create")
    fun create(@RequestHeader("email") email: String,
               @RequestHeader("password") password: String,
               @RequestHeader("phoneNumber") phoneNumber: String,
               @RequestHeader("sex") sex: String,
               @RequestHeader("name") name: String
    ): Map<String, Any> {
        val name = encode(name)
        val sex = encode(sex)
        var flag = false
        val info = when {
            !Check.checkEmail(email) -> {
                "邮箱格式有误！"
            }
            !Check.checkPassword(password) -> {
                "密码安全性过低"
            }
            !Check.checkSex(sex) -> {
                "性别有误！"
            }
            else -> {
                flag = true
                "success"
            }
        }
        if (flag) {
            val user = User()
            user.email = email
            user.phoneNumber = phoneNumber.toLong()
            user.password = password
            user.gender = sex
            user.name = name
            user.update = Date()
            userRepository.save(user)
        }
        return mapOf(
            "status" to info
        )
    }
}