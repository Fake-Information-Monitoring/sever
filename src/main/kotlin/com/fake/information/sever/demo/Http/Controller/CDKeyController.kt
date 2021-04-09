package com.fake.information.sever.demo.Http.Controller

import com.fake.information.sever.demo.DTO.CDKeyRepository
import com.fake.information.sever.demo.DTO.UserRepository
import com.fake.information.sever.demo.Http.Response.Result
import com.fake.information.sever.demo.Http.Response.StatusCode
import com.fake.information.sever.demo.Redis.FakeNewsRedisTemplate
import com.fake.information.sever.demo.Until.AsyncTask.AsyncService
import com.fake.information.sever.demo.Until.JWT.TokenConfig
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
    private lateinit var asyncService: AsyncService

    @PostMapping("/getToken")
    fun createKey(
            @RequestHeader("id") userId:Int
    ): Result<String> {
        val user = userRepository.getOne(userId)
        val token = VerifyToken().getJwt(user = user)
        asyncService.asyncTask {
            userRepository.save(user)
        }
        return Result<String>(
                success = true,
                code = StatusCode.Status200.statusCode,
                msg = "success",
                data = token.toString()
        )
    }
}