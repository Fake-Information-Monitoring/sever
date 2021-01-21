package com.fake.information.sever.demo.Controller

import com.fake.information.sever.demo.DAO.UserRepository
import org.springframework.beans.factory.annotation.Autowired

object Check {
    fun checkEmail(email: String): Boolean {
        return email.matches(Regex(
                "^[a-z0-9A-Z]+[- |a-z0-9A-Z._]+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-z]{2,}$"
        ))
    }
    fun checkPassword(password:String):Boolean{
        return password.matches(Regex(
                "^[a-zA-Z\\d_]{8,}")
        )
    }
    fun checkSex(sex:String):Boolean{
        return (sex == "男" ) or (sex == "man") or (sex == "女") or (sex == "women")
    }
}