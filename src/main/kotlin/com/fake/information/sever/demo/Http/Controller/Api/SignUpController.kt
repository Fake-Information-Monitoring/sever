package com.fake.information.sever.demo.Controller

import com.fake.information.sever.demo.Controller.tools.Check
import com.fake.information.sever.demo.DAO.UserRepository
import com.fake.information.sever.demo.Http.Controller.StatusCode
import com.fake.information.sever.demo.Model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*
import com.fake.information.sever.demo.Http.Response.Result

@RestController
@RequestMapping("v1/signUp", method = [RequestMethod.POST, RequestMethod.GET])
class SignUpController {
    @Autowired
    private lateinit var userRepository: UserRepository

    @ExperimentalStdlibApi
    @PostMapping("/create")
    fun postCreate(
            @RequestHeader("email") email: String,
            @RequestHeader("password") password: String,
            @RequestHeader("phoneNumber") phoneNumber: String,
            @RequestHeader("sex") sex: String,
            @RequestHeader("name") name: String
    ): Result<String> {
        val thisName = Check.encode(name)
        val thisSex = Check.encode(sex)
        val info = Check.checking(email, password, thisSex, thisName, userRepository)
        if (info != true) {
            return Result<String>(
                    success = false,
                    code = StatusCode.Status_401.statusCode,
                    msg = info as String
            )
        }
        val user = User()
        user.email = email
        user.phoneNumber = phoneNumber.toLong()
        user.setPassword(password)
        user.gender = thisSex
        user.name = thisName
        user.update = Date()
        userRepository.save(user)
        return Result<String>(
                success = true,
                code = StatusCode.Status_200.statusCode,
                msg = "success"
        )
    }
}