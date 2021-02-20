package com.fake.information.sever.demo.VerifyCode

import cn.hutool.captcha.CaptchaUtil
import cn.hutool.captcha.ShearCaptcha
import com.fake.information.sever.demo.ThreadPool.FakeNewsThreadPool
import javax.servlet.http.HttpServletRequest
import kotlinx.coroutines.*
import javax.security.auth.Subject
import javax.servlet.http.HttpSession

/*
* @TODO:验证码生成以及验证
*/
class VerifyCode {
    @ObsoleteCoroutinesApi
    fun createCode(session:HttpSession,subject: String): ShearCaptcha {
        val captcha: ShearCaptcha = CaptchaUtil.createShearCaptcha(300, 150, 4, 4)
        session.setAttribute(subject, captcha)
        GlobalScope.launch(FakeNewsThreadPool.threadPool){
            delay(1000*300)
            try {
                session.invalidate()
            }catch (e:IllegalStateException){

            }
        }
        return captcha
    }

    fun verifyCode(session:HttpSession, captcha: String,subject: String): Boolean {
        val shearCaptcha: ShearCaptcha = (session.getAttribute(subject) ?: return true) as ShearCaptcha
        return shearCaptcha.verify(captcha)
    }
}