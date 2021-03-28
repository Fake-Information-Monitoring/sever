package com.fake.information.sever.demo.Until.JWT

import com.fake.information.sever.demo.Http.Until.RSA
import io.jsonwebtoken.SignatureAlgorithm
import javax.crypto.spec.SecretKeySpec

object TokenConfig {
    private val key = RSA.getKeyPair()
    private val secretKey = SecretKeySpec(
        key?.private?.encoded, SignatureAlgorithm.HS256.jcaName
    )
    private const val TOKEN_EXPIRE_MOUTH = 1000 * 60 * 60 * 24 * 30 // 1个月
    private const val TOKEN_EXPIRE_YEAR = TOKEN_EXPIRE_MOUTH * 12 // 一年

    enum class TokenVerifyCode(var verifyCode: Int) {
        Success(0),
        Expired(1),
        UnKnow(2),
        Malformed(3),
        IllegalArgument(4)
    }
}