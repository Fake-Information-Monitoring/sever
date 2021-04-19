package com.fake.information.sever.demo.Until.VerifyCode

import cn.hutool.captcha.CaptchaUtil
import cn.hutool.captcha.LineCaptcha
import cn.hutool.captcha.ShearCaptcha
import com.fake.information.sever.demo.Redis.FakeNewsRedisTemplate
import kotlinx.coroutines.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.TimeUnit
import javax.servlet.http.HttpSession
import com.fake.information.sever.demo.Until.AsyncTask.AsyncService
import java.awt.Font

/*
* @TODO:验证码生成以及验证
*/
@RestController
class VerifyCode(private var redisTemplate: FakeNewsRedisTemplate) {


    @ObsoleteCoroutinesApi
    fun createCode(session: HttpSession, subject: String): LineCaptcha {

        val captcha: LineCaptcha? = CaptchaUtil
                .createLineCaptcha(200, 90)
            redisTemplate.setRedis(session.id + subject, captcha!!)
            redisTemplate.setTime(
                session.id + subject,
                1000 * 300,
                TimeUnit.SECONDS
            )
        return captcha
    }

    fun verifyCode(session: HttpSession, captcha: String, subject: String): Boolean {
        val shearCaptcha: ShearCaptcha = (
                redisTemplate.getRedis(session.id + subject) ?: return false
                ) as ShearCaptcha
        return shearCaptcha.verify(captcha)
    }
}