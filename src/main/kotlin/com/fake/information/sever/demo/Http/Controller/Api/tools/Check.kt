package com.fake.information.sever.demo.Controller.tools

import com.fake.information.sever.demo.DAO.UserRepository
import com.fake.information.sever.demo.Http.Controller.StatusCode
import com.fake.information.sever.demo.Model.User
import com.fake.information.sever.demo.Http.Response.Result
import java.net.URLDecoder

object Check {
    fun checkEmail(email: String): Boolean {
        return email.matches(Regex(
                "^[a-z0-9A-Z]+[- |a-z0-9A-Z._]+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-z]{2,}$"
        ))
    }

    fun checkPassword(password: String): Boolean {
        return password.matches(Regex(
                "^[a-zA-Z\\d_]{8,}")
        )
    }

    fun checkSex(sex: String): Boolean {
        return (sex == "男") or (sex == "女")
    }

    @ExperimentalStdlibApi
    fun checkAccount(user: User?, password: String): Result<String> {
        var success = false
        val error = when {
            user == null -> {
                "用户不存在"
            }
            password != user.getPassword() -> {
                //TODO：生成验证码并加入Session
                "密码错误"
            }
            else -> {
                success = true
                "login success"
            }
        }
        return Result<String>(
                success = success,
                code = if (success) StatusCode.Status_200.statusCode else StatusCode.Status_401.statusCode,
                msg = error
        )
    }

    private fun checkUserByName(userRepository: UserRepository, name: String): Boolean {
        return userRepository.findByName(name) == null
    }

    private fun checkUserByEmail(userRepository: UserRepository, email: String): Boolean {
        return userRepository.findByEmail(email) == null
    }

    fun encode(str: String): String {
        return URLDecoder.decode(str, "utf-8")
    }

    fun checking(email: String = "",
                         password: String = "",
                         sex: String = "",
                         name: String = "",
                         userRepository: UserRepository
    ): String {
        return when {
            (!Check.checkEmail(email) && email.isNotEmpty()) -> "邮箱格式有误！"
            (!Check.checkPassword(password) && password.isNotEmpty()) -> "密码安全性过低"
            (!Check.checkSex(sex) && sex.isNotEmpty()) -> "性别有误！"
            (!checkUserByName(userRepository,name) && name.isNotEmpty()) -> "该昵称已存在"
            (!checkUserByEmail(userRepository,email) && email.isNotEmpty()) -> "该邮箱已被使用"
            else -> "OK"
        }
    }
}