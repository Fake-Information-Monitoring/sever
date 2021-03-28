package com.fake.information.sever.demo.Until.JWT

import com.fake.information.sever.demo.Model.CDKey
import com.fake.information.sever.demo.Model.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwt

interface FakeNewsWebToken {
    fun getJwt(user: User): CDKey

    fun verifyJwt(jwt: String): Jws<Claims>?

    fun decodeJwt(jwt: String): Jws<Claims>?
}