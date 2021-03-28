package com.fake.information.sever.demo.Until.JWT

import com.fake.information.sever.demo.Model.User

class VerifyToken:JWT {
    override fun getJwt(user: User): String {
        TODO("Not yet implemented")
    }

    override fun verifyJwt(jwt: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun getJwtValidity() {
        TODO("Not yet implemented")
    }
}