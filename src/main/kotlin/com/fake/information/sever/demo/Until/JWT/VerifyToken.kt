package com.fake.information.sever.demo.Until.JWT


import com.fake.information.sever.demo.DTO.CDKeyRepository
import com.fake.information.sever.demo.Model.CDKey
import com.fake.information.sever.demo.Model.User
import com.fake.information.sever.demo.Until.JWT.TokenConfig.JwtExpiredException
import io.jsonwebtoken.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import java.security.SignatureException
import java.util.*


class VerifyToken : FakeNewsWebToken {

    override fun getJwt(user: User): CDKey {
        val now = System.currentTimeMillis()
        val key = CDKey()
        val token = Jwts.builder().setHeaderParam("user", user.id)
            .setHeaderParam("keyId", key.id)
            .setExpiration(
                Date(
                    now + TokenConfig.TOKEN_EXPIRE_MOUTH
                )
            )
            .setNotBefore(Date(now))
            .signWith(TokenConfig.secretKey)
        key.user = user
        user.keyList.add(key)
        key.key = token.compact()
        return key
    }

    @Throws(JwtExpiredException::class)
    override fun verifyJwt(jwt: String): Jws<Claims>?{
        try {
            return decodeJwt(jwt)
        } catch (e: ExpiredJwtException) {
            throw JwtExpiredException("jwt已过期！")
        }catch (e: SignatureException){
            throw TokenConfig.JwtFailureException("jwt解码异常")
        }catch (e: MalformedJwtException){
            throw TokenConfig.JwtMalformedException("无效的Jwt")
        } catch (e:Exception){
            throw Exception("未知错误${e.message}")
        }
    }

    override fun decodeJwt(jwt: String): Jws<Claims>? {
        return Jwts.parserBuilder().setSigningKey(TokenConfig.secretKey).build()
            .parseClaimsJws(jwt)
    }

}