package com.fake.information.sever.demo.VerifyCode

import cn.hutool.captcha.CaptchaUtil
import cn.hutool.captcha.ShearCaptcha
import com.fake.information.sever.demo.SessionManager.SessionManage
import javax.servlet.http.HttpServletRequest

/*
* @TODO:验证码生成以及验证
*/
class VerifyCode {
    fun createCode(request: HttpServletRequest): ShearCaptcha {
        val captcha: ShearCaptcha = CaptchaUtil.createShearCaptcha(300, 150, 4, 4)
        SessionManage(request).createSession(request.requestedSessionId, captcha)
        return captcha
    }

    fun verifyCode(request: HttpServletRequest, captcha: String): Boolean {
        val session: ShearCaptcha = (SessionManage(request)
                .getSessionValue(request.requestedSessionId) ?: return false
                ) as ShearCaptcha
        return session.verify(captcha)
    }
}