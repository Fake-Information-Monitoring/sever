package com.fake.information.sever.demo.Http.Controller

import com.fake.information.sever.demo.DAO.UserRepository
import com.fake.information.sever.demo.Http.Response.ReturnData
import com.fake.information.sever.demo.Model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController

class LoginController {
//    @Autowired
//    private lateinit var userRepository: UserRepository
//
//    @Autowired
//    private var stringRedisTemplate : StringRedisTemplate
//
//    @RequestMapping("/login")
//    fun
//
//    @RequestMapping(value = ["/getUserInfo"])
//    open fun get(userId: Long): ReturnData? {
//        val user: User = userService.findUserByUserId(userId)
//        return if (user != null) {
//            ReturnData(StatusCode.REQUEST_SUCCESS, user, "查询成功！")
//        } else {
//            throw MyException(StatusCode.USER_NOT_EXIST, "用户不存在！")
//        }
//    }

//    @RequestMapping("/add")
//    fun saveUser(): String {
//        val user = User()
//        user.name ="userName"
//        user.email = "email"
//        user.gender = "gender"
//        user.password = "password"
//        user.phoneNumber = 1233
//        user.update = Date()
//        userRepository.save(user)
//        return "success"
//    }

}