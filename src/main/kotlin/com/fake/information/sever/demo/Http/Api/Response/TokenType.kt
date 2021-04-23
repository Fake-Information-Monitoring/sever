package com.fake.information.sever.demo.Http.Api.Response

enum class TokenType(var type:String) {
    RUMORS("Rumor") {
        override fun toString(): String {
            return type
        }
    },
    ZOMBIES("Zombies") {
        override fun toString(): String {
            return type
        }
    },
    SENSITIVE_WORD("Sensitive") {
        override fun toString(): String {
            return type
        }
    },
    TEST("Test"){
        override fun toString():String{
            return type
        }
    };
    companion object{
        fun hasValue(value:String):Boolean{
            values().forEach {
                if(it.toString() == value) return true
            }
            return false
        }
    }

}