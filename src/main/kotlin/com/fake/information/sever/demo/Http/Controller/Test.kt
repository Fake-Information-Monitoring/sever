package com.fake.information.sever.demo.Http.Controller

import com.fake.information.sever.demo.DAO.UserRepository
import com.fake.information.sever.demo.Model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.DateTimeException
import java.util.*


@RestController
@RequestMapping("/test")
class Test {
    @Autowired
    private lateinit var userRepository: UserRepository

    @RequestMapping("/add")
    fun saveUser(): String {
        val user = User()
        user.name ="userName"
        user.email = "email"
        user.gender = "gender"
        user.password = "password"
        user.phoneNumber = 1233
        user.update = Date()
        userRepository.save(user)
        return "success"
    }

}