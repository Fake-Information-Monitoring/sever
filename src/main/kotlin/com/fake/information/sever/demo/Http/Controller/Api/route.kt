package com.fake.information.sever.demo.Http.Controller.Api

import com.fake.information.sever.demo.Http.Controller.BuildError
import com.fake.information.sever.demo.DAO.UserRepository
import com.fake.information.sever.demo.Model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@RestController
@RequestMapping("/api/v1",method = [RequestMethod.POST])
class ProductServiceController{
//    @Autowired
//    private lateinit var userRepository: UserRepository
//    private fun checkEmail(email: String): Boolean {
//        return email.matches(Regex("^[a-z0-9A-Z]+[- |a-z0-9A-Z._]+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-z]{2,}$"))
//    }
//
//
//    @ExperimentalStdlibApi
//    @PostMapping("/login_with_email")
//    fun loginWithEmail(@RequestParam("account") account: String,
//              @RequestParam("password") password: String,
//              @RequestParam("CAPTCHA") captcha: String
//    ): String {
//        //TODO:检验验证码是否正确
//            var tempUser: User? = null
//            if (checkEmail(account)) {
//                tempUser = userRepository.findByEmail(account)
//            }else {
//                return BuildError.buildErrorInfo("格式有误")
//            }
//            if (tempUser == null) {
//                return BuildError.buildErrorInfo("用户不存在")
//            }
//        //TODO:添加Session
//            return "success"
//    }
//
//    @ExperimentalStdlibApi
//    @PostMapping("/login_with_phone")
//    fun loginWithPhone(@RequestParam("account") account: String,
//                       @RequestParam("password") password: String,
//                       @RequestParam("CAPTCHA") captcha: String
//    ): String {
//        //TODO:检验验证码是否正确
//        var tempUser: User? = null
//        try {
//            tempUser = userRepository.findByPhoneNumber(account.toLong())
//        }catch (e:NumberFormatException){
//            return BuildError.buildErrorInfo("账号格式有误")
//        }
//        if (tempUser == null) {
//            return BuildError.buildErrorInfo("用户不存在")
//        }
//        //TODO:添加Session
//        return "success"
//    }
//
//    @PostMapping("/verifyCode")
//    fun verifyCode(request: HttpServletRequest, response: HttpServletResponse){
//            //TODO:返回验证码
//    }

}