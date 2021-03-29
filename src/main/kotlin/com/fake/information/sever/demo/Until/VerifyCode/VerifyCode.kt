package com.fake.information.sever.demo.Until.VerifyCode

import cn.hutool.captcha.CaptchaUtil
import cn.hutool.captcha.ShearCaptcha
import com.fake.information.sever.demo.Redis.FakeNewsRedisTemplate
import com.fake.information.sever.demo.Until.AsyncTask.FakeNewsAsyncService
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
    fun createCode(session: HttpSession, subject: String): ShearCaptcha {
        val captcha: ShearCaptcha = CaptchaUtil
                .createShearCaptcha(300, 150, 4, 4)
        FakeNewsAsyncService().asyncTask {
            redisTemplate.setRedis(session.id + subject, captcha)
            redisTemplate.setTime(
                session.id + subject,
                1000 * 300,
                TimeUnit.SECONDS
            )
        }
        return captcha
    }

    fun verifyCode(session: HttpSession, captcha: String, subject: String): Boolean {
        val shearCaptcha: ShearCaptcha = (
                redisTemplate.getRedis(session.id + subject) ?: return false
                ) as ShearCaptcha
        return shearCaptcha.verify(captcha)
    }
}