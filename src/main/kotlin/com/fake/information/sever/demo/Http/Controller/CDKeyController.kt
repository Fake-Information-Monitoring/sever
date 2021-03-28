package com.fake.information.sever.demo.Http.Controller

import com.fake.information.sever.demo.DTO.CDKeyRepository
import com.fake.information.sever.demo.DTO.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/cdKey")
class CDKeyController {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var cdKeyRepository: CDKeyRepository


//    @PostMapping("/createKey")
//    fun createKey(
//            @RequestHeader("id") userId:Int,
//            @RequestHeader("isMonth") isMonth:Boolean
//    ): Result<String> {
//        val user = userRepository.getOne(userId)
//        val cdkey = JWTManage.createJWT("cdkey", mapOf(
//                    "user" to user.id, "date" to Date()
//            ),isMonth = isMonth)
//        user.keyList.add(cdkey)
//        cdkey.user = user
//        userRepository.save(user)
//        return Result<String>(
//                success = true,
//                code = StatusCode.Status200.statusCode,
//                msg = "success",
//                data = cdkey.key
//        )
//
//    }
}