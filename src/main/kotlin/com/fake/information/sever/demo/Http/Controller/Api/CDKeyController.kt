package com.fake.information.sever.demo.Http.Controller.Api

import com.fake.information.sever.demo.DAO.CDKeyRepository
import com.fake.information.sever.demo.DAO.UserRepository
import com.fake.information.sever.demo.Http.Controller.StatusCode
import com.fake.information.sever.demo.Until.JWT.JWTManage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*
import com.fake.information.sever.demo.Http.Response.Result
@RestController
@RequestMapping("/v1/cdKey")
class CDKeyController {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var cdKeyRepository: CDKeyRepository


    @PostMapping("/createKey")
    fun createKey(
            @RequestHeader("id") userId:Int,
            @RequestHeader("type") type:Boolean
    ): Result<String> {
        val user = userRepository.getOne(userId)
        val cdkey = JWTManage.createJWT("cdkey", mapOf(
                    "user" to user.id, "date" to Date()
            ),isMonth = type)
        user.keyList.add(cdkey)
        cdkey.user = user
        userRepository.save(user)
        return Result<String>(
                success = true,
                code = StatusCode.Status200.statusCode,
                msg = "success",
                data = cdkey.key
        )

    }
}