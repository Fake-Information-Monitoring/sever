package com.fake.information.sever.demo.Controller.tools

import com.fake.information.sever.demo.Http.Controller.StatusCode
import com.fake.information.sever.demo.Model.User
import com.fake.information.sever.demo.Http.Response.Result
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

}