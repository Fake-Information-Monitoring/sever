package com.fake.information.sever.demo.Http.Api.Controller

import com.fake.information.sever.demo.DTO.CommitRepository
import com.fake.information.sever.demo.DTO.UserRepository
import com.fake.information.sever.demo.Config.Redis.FakeNewsRedisTemplate
import com.fake.information.sever.demo.Http.Api.Response.StatusCode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import com.fake.information.sever.demo.Http.Api.Response.Result
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import javax.servlet.http.HttpSession

@Api(value = "用户信息管理接口")
@RestController
@RequestMapping("/v1/getInfo")
class GetUserInfo {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var commitRepository: CommitRepository

    @Autowired
    private lateinit var redisTemplate: FakeNewsRedisTemplate

    @ExperimentalStdlibApi
    @GetMapping("/")
    @ApiOperation("获取用户信息")
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
    @ApiOperation("获取用户头像OSSURL")
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
    @ApiOperation("获取某个特定的提交验证记录")
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