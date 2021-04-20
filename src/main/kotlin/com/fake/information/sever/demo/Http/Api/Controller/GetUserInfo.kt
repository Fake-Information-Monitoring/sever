package com.fake.information.sever.demo.Controller

import com.fake.information.sever.demo.DTO.CommitRepository
import com.fake.information.sever.demo.DTO.UserRepository
import com.fake.information.sever.demo.Redis.FakeNewsRedisTemplate
import com.fake.information.sever.demo.Http.Api.Response.StatusCode
import com.fake.information.sever.demo.Model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import com.fake.information.sever.demo.Http.Api.Response.Result
import javax.servlet.http.HttpSession

@RestController
@RequestMapping("/v1/getInfo", method = [RequestMethod.GET])
class GetUserInfo {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var commitRepository: CommitRepository

    @Autowired
    private lateinit var redisTemplate: FakeNewsRedisTemplate

    @ExperimentalStdlibApi
    @GetMapping("/")
    fun getUserInfo(
            session: HttpSession
    ): Any {
        return try {
            val user = redisTemplate.getUserId(session)
            Result(
                    success = true,
                    code = StatusCode.Status200.statusCode,
                    msg = "success",
                    data = userRepository.findById(user).get()
            )

        } catch (e: NoSuchElementException) {
            Result<String>(
                    success = false,
                    code = StatusCode.Status502.statusCode,
                    msg = e.toString()
            )
        }
    }

    @GetMapping("/avatar")
    fun getUserAvatar(
            session: HttpSession
    ): Any {
        return try {
            val user = redisTemplate.getUserId(session)
            Result(
                    success = true,
                    code = StatusCode.Status200.statusCode,
                    msg = "success",
                    data = userRepository.getOne(user).avatar!!
            )
        } catch (e: NoSuchElementException) {
            Result(
                    success = false,
                    code = StatusCode.Status502.statusCode,
                    msg = e.toString()
            )
        }
    }

    @ExperimentalStdlibApi
    @GetMapping("/{commit}")
    fun getCommit(
                  @PathVariable commit: Int,
                  session: HttpSession
    ): Any {
        return try {
            val user = redisTemplate.getUserId(session)
            val commit = commitRepository.getOne(commit)
            if (commit.user?.id == user)
                commit.indexOSSUrl!!
            else
                throw IllegalAccessException("您没有权限")
        } catch (e: NoSuchElementException) {
            Result<String>(
                    success = false,
                    code = StatusCode.Status502.statusCode,
                    msg = e.toString()
            )
        }
    }
}