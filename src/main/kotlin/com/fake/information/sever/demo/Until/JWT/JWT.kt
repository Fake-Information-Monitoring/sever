package com.fake.information.sever.demo.Until.JWT

import com.fake.information.sever.demo.Model.CDKey
import com.fake.information.sever.demo.Model.User

interface JWT {
    fun getJwt(user: User): String

    fun verifyJwt(jwt: String): Boolean

    // 获取Jwt剩余有效期
    fun getJwtValidity()
}