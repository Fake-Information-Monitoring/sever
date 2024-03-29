package com.fake.information.sever.demo.Until.VerifyCode

import cn.hutool.captcha.CaptchaUtil
import cn.hutool.captcha.LineCaptcha
import com.fake.information.sever.demo.Config.Redis.FakeNewsRedisTemplate
import kotlinx.coroutines.*
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.TimeUnit
import javax.servlet.http.HttpSession

/*
* @TODO:验证码生成以及验证
*/
@RestController
class VerifyCode(private var redisTemplate: FakeNewsRedisTemplate) {


    @ObsoleteCoroutinesApi
    fun createCode(session: HttpSession, subject: String): LineCaptcha {

        val captcha: LineCaptcha? = CaptchaUtil
                .createLineCaptcha(150, 60)
            redisTemplate.setRedis(session.id + subject, captcha!!)
            redisTemplate.setTime(
                session.id + subject,
                1000 * 60,
                TimeUnit.SECONDS
            )
        return captcha
    }

    fun verifyCode(session: HttpSession, captcha: String, subject: String): Boolean {
        val shearCaptcha: LineCaptcha = (
                redisTemplate.getRedis(session.id + subject) ?: return false
                ) as LineCaptcha
        return shearCaptcha.verify(captcha)
    }
}