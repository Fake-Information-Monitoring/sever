package com.fake.information.sever.demo.Controller.tools

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
        return (sex == "男" ) or (sex == "女")
    }
}