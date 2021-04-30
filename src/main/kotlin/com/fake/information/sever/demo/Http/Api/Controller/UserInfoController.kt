package com.fake.information.sever.demo.Http.Api.Controller

import com.fake.information.sever.demo.DTO.CommitRepository
import com.fake.information.sever.demo.DTO.UserRepository
import com.fake.information.sever.demo.Config.Redis.FakeNewsRedisTemplate
import com.fake.information.sever.demo.Controller.tools.Check
import com.fake.information.sever.demo.DTO.PersonCertifiedRepository
import com.fake.information.sever.demo.Http.Api.Response.StatusCode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import com.fake.information.sever.demo.Http.Api.Response.Result
import com.fake.information.sever.demo.Model.PersonCertified
import com.fake.information.sever.demo.Model.User
import com.fake.information.sever.demo.Until.AsyncTask.AsyncService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.hibernate.criterion.NotEmptyExpression
import javax.security.auth.login.AccountNotFoundException
import javax.servlet.http.HttpSession
import javax.websocket.server.PathParam

@Api(value = "用户信息管理接口")
@RestController
@RequestMapping("/v1/getInfo")
class UserInfoController {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var commitRepository: CommitRepository

    @Autowired
    private lateinit var redisTemplate: FakeNewsRedisTemplate

    @Autowired
    private lateinit var personCertifiedRepository: PersonCertifiedRepository

    @Autowired
    private lateinit var asyncService: AsyncService

    @PostMapping("/personCertified")
    @ApiOperation("个人认证信息")
    fun personCertified(
        @RequestBody param: Map<String, Any>,
        session: HttpSession
    ): Result<String> {
        val user = userRepository.findById(
            redisTemplate.getUserId(session)
        ).get()
        if (user.personCertified != null) {
            throw AccountNotFoundException("您已认证")
        }

        val cardId = param["cardId"].toString()
        if (!Check.checkCardId(cardId)) {
            throw IllegalArgumentException("身份证号格式有误")
        }
        asyncService.asyncTask{
        val work = param["work"].toString()
        val name = param["name"].toString()
        val personCertified = PersonCertified()
        personCertified.user = user
        personCertified.name = name
        personCertified.cardId = cardId
        personCertified.work = work
        user.personCertified = personCertified
        personCertifiedRepository.save(personCertified)
        }
        return Result(
            success = true,
            code = StatusCode.Status200.statusCode,
            msg = "success"
        )
    }

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
    @GetMapping("/getUserKey")
    @ApiOperation("获取用户Key")
    fun getKey(
        session: HttpSession
    ): Result<String> {
        val user: User = userRepository.getOne(redisTemplate.getUserId(session))
        return Result<String>(
            success = true,
            code = StatusCode.Status200.statusCode,
            msg = user.keyList
        )
    }
    @ExperimentalStdlibApi
    @GetMapping("/commit")
    @ApiOperation("获取某个特定的提交验证记录")
    fun getCommit(
        commit: Int,
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