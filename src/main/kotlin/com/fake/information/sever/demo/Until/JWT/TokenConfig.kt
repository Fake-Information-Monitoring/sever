package com.fake.information.sever.demo.Until.JWT

import com.fake.information.sever.demo.Http.Until.RSA
import io.jsonwebtoken.SignatureAlgorithm
import javax.crypto.spec.SecretKeySpec

object TokenConfig {
    val key = RSA.getKeyPair()
    private val signatureAlgorithm = SignatureAlgorithm.HS256
    val secretKey = SecretKeySpec(
        key?.private?.encoded, signatureAlgorithm.jcaName
    )
    const val TOKEN_EXPIRE_MOUTH = 1000 * 60 * 60 * 24 * 30 // 1个月
    const val TOKEN_EXPIRE_YEAR = TOKEN_EXPIRE_MOUTH * 12 // 一年
    const val TOKEN_GET_COUNT = 500
    enum class TokenVerifyCode(var verifyCode: Int) {
        Success(0),
        Expired(1),
        UnKnow(2),
        Malformed(3),
        IllegalArgument(4)
    }

    class JwtExpiredException(message: String?) : Exception(message)
    class JwtFailureException(message: String?) : Exception(message)
    class JwtMalformedException(message: String?): Exception(message)
}