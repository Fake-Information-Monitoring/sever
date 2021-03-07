package com.fake.information.sever.demo.Http.Controller.Api

import com.fake.information.sever.demo.Controller.tools.Check
import com.fake.information.sever.demo.DAO.UserRepository
import com.fake.information.sever.demo.EmailUntil.MailService
import com.fake.information.sever.demo.Http.Controller.StatusCode
import com.fake.information.sever.demo.Http.Response.Result
import com.fake.information.sever.demo.VerifyCode.VerifyCode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpSession

@RestController
@RequestMapping("/v1/forward", method = [RequestMethod.GET, RequestMethod.PUT])
class ForwardPassword {
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var mailService: MailService
    @RequestMapping("/",method = [RequestMethod.GET])
    fun forwardPassword(session:HttpSession,@RequestHeader("verifyCode") verifyCode: String): Result<String> {
        if (!VerifyCode().verifyCode(session, verifyCode,"emailCode")){
            val info = "验证码错误"
            return Result<String>(
                    success = false,
                    code = StatusCode.Status401.statusCode,
                    msg = info as String
            )
        }
        session.setAttribute("forward",true)
        return Result<String>(
                success = true,
                code = StatusCode.Status200.statusCode,
                msg = "success"
        )
    }
    @RequestMapping("/change",method = [RequestMethod.PUT])
    fun changePassword(session:HttpSession,@RequestHeader("email")email: String,@RequestHeader("changePassword") password: String): Result<String> {
        if (session.getAttribute("forward")!=true){
            return Result<String>(
                    success = false,
                    code = StatusCode.Status401.statusCode,
                    msg = "未通过验证！"
            )
        }
        if (!Check.checkPassword(password)){
            return Result<String>(
                    success = false,
                    code = StatusCode.Status401.statusCode,
                    msg = "密码不合法！"
            )
        }
        val user = userRepository.findByEmail(email)
        user!!.setPassword(password)
        userRepository.save(user)
        return Result<String>(
                success = true,
                code = StatusCode.Status200.statusCode,
                msg = "success"
        )
    }
    @GetMapping("/sendEmail")
    fun forwardPasswordCheck(@RequestHeader("email") email:String, session:HttpSession): Result<String> {
        val verifyCode = VerifyCode().createCode(session,"emailCode")
        mailService.sendSimpleMail(email,"验证码,五分钟内有效",verifyCode.code)
        return Result<String>(
                success = true,
                code = StatusCode.Status200.statusCode,
                msg = "success"
        )
    }

}