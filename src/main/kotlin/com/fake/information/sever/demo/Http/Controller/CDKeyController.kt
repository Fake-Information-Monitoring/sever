package com.fake.information.sever.demo.Http.Controller

import com.fake.information.sever.demo.DTO.CDKeyRepository
import com.fake.information.sever.demo.DTO.UserRepository
import com.fake.information.sever.demo.Http.Response.Result
import com.fake.information.sever.demo.Http.Response.StatusCode
import com.fake.information.sever.demo.Redis.FakeNewsRedisTemplate
import com.fake.information.sever.demo.Until.JWT.VerifyToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/cdKey")
class CDKeyController {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var redisTemplate: FakeNewsRedisTemplate

    @Autowired
    private lateinit var cdKeyRepository: CDKeyRepository

    @Async
    @PostMapping("/getToken")
    fun createKey(
            @RequestHeader("id") userId:Int,
            @RequestHeader("isMonth") isMonth:Boolean
    ): Result<String> {
        val user = userRepository.getOne(userId)
        val token = VerifyToken().getJwt(user = user)
        token.allCount = 10000
        token.useCount = 0
        userRepository.save(user)
        return Result<String>(
                success = true,
                code = StatusCode.Status200.statusCode,
                msg = "success",
                data = token.toString()
        )
    }

//    fun verifyToken(token:String){
//        val key = VerifyToken().verifyJwt(token)?.body
//        val keyModel = cdKeyRepository.findById(key?.get("keyId").toString().toInt()).get()
//        val count =  redisTemplate.getRedis(keyModel.id.toString())?:redisTemplate
//            .setRedis(keyModel.id.toString(),1)
//
//        redisTemplate.setRedis(keyModel.id,)
//        if ()
//    }
}