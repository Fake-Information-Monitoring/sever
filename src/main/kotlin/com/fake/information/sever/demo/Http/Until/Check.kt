package com.fake.information.sever.demo.Controller.tools

import com.fake.information.sever.demo.DTO.UserRepository
import com.fake.information.sever.demo.Model.User
import com.fake.information.sever.demo.Until.VerifyCode.VerifyCode
import kotlinx.coroutines.ObsoleteCoroutinesApi
import java.net.URLDecoder
import javax.servlet.http.HttpSession

object Check {
    fun checkEmail(email: String): Boolean {
        return email.matches(
            Regex(
                "^[a-z0-9A-Z]+[- |a-z0-9A-Z._]+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-z]{2,}$"
            )
        )
    }

    fun checkCardId(cardId: String): Boolean {
        return cardId.matches(
            Regex(
                "^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$"
            )
        )
    }

    fun checkPassword(password: String): Boolean {
        return password.matches(
            Regex(
                "^[a-zA-Z\\d_]{8,}"
            )
        )
    }

    fun checkSex(sex: String): Boolean {
        return (sex == "男") or (sex == "女")
    }

    @ObsoleteCoroutinesApi
    @ExperimentalStdlibApi
    fun checkAccount(verifyCode: VerifyCode, session: HttpSession, user: User?, password: String): Boolean {
        val info = when {
            user == null -> {
                "用户不存在"
            }
            password != user.getPassword() -> {
                "密码错误"
            }
            else -> {
                null
            }
        }
        if (info != null) {
            verifyCode.createCode(session, "verifyCode")
            throw IllegalArgumentException(info)
        }
        return true
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

    fun checking(
        email: String = "",
        password: String = "",
        name: String = "",
        userRepository: UserRepository
    ): Boolean {
        when {
            (!Check.checkEmail(email) && email.isNotEmpty()) ->
                throw IllegalArgumentException("邮箱格式有误！")
            (!Check.checkPassword(password) && password.isNotEmpty()) ->
                throw IllegalArgumentException("密码安全性过低")
            (!checkUserByName(userRepository, name) && name.isNotEmpty()) ->
                throw IllegalArgumentException("该昵称已存在")
            (!checkUserByEmail(userRepository, email) && email.isNotEmpty()) ->
                throw IllegalArgumentException("该邮箱已被使用")
            else -> return true
        }
    }
}