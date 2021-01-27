package com.fake.information.sever.demo.JWT


import com.fake.information.sever.demo.Http.Controller.Api.tools.RSA
import com.fake.information.sever.demo.Model.CDKey
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.jsonwebtoken.*
import io.jsonwebtoken.gson.io.GsonDeserializer
import io.jsonwebtoken.gson.io.GsonSerializer
import java.util.*
import javax.crypto.spec.SecretKeySpec


object JWTManage {
    private val key = RSA.getKeyPair()
    private val secretKey = SecretKeySpec(key?.private?.encoded, SignatureAlgorithm.HS256.jcaName)
    private const val TOKEN_EXPIRE_MOUTH = 1000 * 60 * 60 * 24 * 30 // 1个月
    private const val TOKEN_EXPIRE_YEAR = TOKEN_EXPIRE_MOUTH * 12 // 一年
    var gson: Gson = GsonBuilder().disableHtmlEscaping().create()

    enum class TokenVerifyCode(var verifyCode: Int) {
        Success(0),
        Expired(1),
        UnKnow(2),
        Malformed(3),
        IllegalArgument(4)
    }

    fun createJWT(subject: String, params: Map<String, Any?>, isMonth: Boolean = true): CDKey {
        val cdKey = CDKey()
        val expire =Date(Date().time + if (isMonth) TOKEN_EXPIRE_MOUTH else TOKEN_EXPIRE_YEAR)
        val jwt = Jwts.builder().serializeToJsonWith(GsonSerializer(gson)).setId(cdKey.id.toString()).setIssuedAt(Date()).setExpiration(expire).setSubject(subject).signWith(secretKey)
                .setClaims(params).compact()
        cdKey.key = jwt
        return cdKey
    }

    fun verifyToken(token: String): Int {
        return try {
            Jwts.parserBuilder().deserializeJsonWith(GsonDeserializer(gson)).setSigningKey(secretKey).build().parseClaimsJws(token)
            TokenVerifyCode.Success.verifyCode
        } catch (e: ExpiredJwtException) {
            e.printStackTrace()
            TokenVerifyCode.Expired.verifyCode
        } catch (e: UnsupportedJwtException) {
            e.printStackTrace()
            TokenVerifyCode.UnKnow.verifyCode
        } catch (e: MalformedJwtException) {
            e.printStackTrace()
            TokenVerifyCode.Malformed.verifyCode
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            TokenVerifyCode.IllegalArgument.verifyCode
        }
    }

    fun decodeJWT(token: String): Map<String, Any> {
        return Jwts.parserBuilder().deserializeJsonWith(GsonDeserializer(gson)).setSigningKey(secretKey).build().parseClaimsJws(token).body
    }
}

fun main() {
    val t = JWTManage.createJWT("fuck", mapOf("fuck" to "fuck"))
    println(t.key)
}