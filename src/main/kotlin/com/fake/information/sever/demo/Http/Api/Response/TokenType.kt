package com.fake.information.sever.demo.Http.Api.Response

enum class TokenType(var type:String) {
    RUMORS("谣言"),
    ZOMBIES("僵尸用户"),
    SENSITIVE_WORD("敏感词");
    companion object{
        fun hasValue(value:String):Boolean{
            values().forEach {
                if(it.name == value) return true
            }
            return false
        }
    }

}