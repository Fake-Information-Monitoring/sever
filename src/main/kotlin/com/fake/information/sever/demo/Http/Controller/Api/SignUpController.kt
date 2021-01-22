package com.fake.information.sever.demo.Controller

import com.fake.information.sever.demo.Controller.tools.Check
import com.fake.information.sever.demo.DAO.UserRepository
import com.fake.information.sever.demo.Http.Controller.StatusCode
import com.fake.information.sever.demo.Model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.net.URLDecoder
import java.util.*
import com.fake.information.sever.demo.Http.Response.Result

@RestController
@RequestMapping("v1/signUp", method = [RequestMethod.POST, RequestMethod.GET])
class SignUpController {
    @Autowired
    private lateinit var userRepository: UserRepository
    private fun checkUserByName(name: String): Boolean {
        return userRepository.findByName(name) == null
    }

    private fun checkUserByEmail(email: String): Boolean {
        return userRepository.findByEmail(email) == null
    }

    private fun encode(str: String): String {
        return URLDecoder.decode(str, "utf-8")
    }

    private fun checking(email: String = "",
                 password: String = "",
                 sex: String = "",
                 name: String = ""): String {
        return when {
            (!Check.checkEmail(email) && email.isNotEmpty()) -> "邮箱格式有误！"
            (!Check.checkPassword(password) && password.isNotEmpty()) -> "密码安全性过低"
            (!Check.checkSex(sex) && sex.isNotEmpty()) -> "性别有误！"
            (!checkUserByName(name) && name.isNotEmpty()) -> "该昵称已存在"
            (!checkUserByEmail(email) && email.isNotEmpty()) -> "该邮箱已被使用"
            else -> "OK"
        }
    }


    @ExperimentalStdlibApi
    @PostMapping("/create")
    fun create(@RequestHeader("email") email: String,
                        @RequestHeader("password") password: String,
                        @RequestHeader("phoneNumber") phoneNumber: String,
                        @RequestHeader("sex") sex: String,
                        @RequestHeader("name") name: String
    ): String {
        val thisName = encode(name)
        val thisSex = encode(sex)
        val info = checking(email, password, thisSex, thisName)
        var flag = false
        if (info == "OK") {
            val user = User()
            user.email = email
            user.phoneNumber = phoneNumber.toLong()
            user.setPassword(password)
            user.gender = thisSex
            user.name = thisName
            user.update = Date()
            userRepository.save(user)
            flag = true
        }
        return Result<String>(
                 success = flag,
                code = if (flag) StatusCode.Status_200.statusCode else StatusCode.Status_401.statusCode,
                msg = info
        ).toJson()
    }
}