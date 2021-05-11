package com.fake.information.sever.demo.Http.Api.Controller

import com.fake.information.sever.demo.DAO.UserRepository
import com.fake.information.sever.demo.Http.Api.Response.StatusCode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import com.fake.information.sever.demo.Http.Api.Response.Result
import com.fake.information.sever.demo.Config.Redis.FakeNewsRedisTemplate
import com.fake.information.sever.demo.Until.AsyncTask.AsyncService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

@RestController
@RequestMapping("/v1/update", method = [RequestMethod.PUT])
@Api("用户个人信息更新")
class UpdateController {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var asyncService: AsyncService

    @Autowired
    private lateinit var redisTemplate: FakeNewsRedisTemplate

    @ExperimentalStdlibApi
    @PutMapping("/update")
    @ApiOperation("信息更新")
    fun putUpdate(
        @RequestBody params: Map<String, Any>,
        response: HttpServletResponse,
        session: HttpSession
    ): Result<String> {
        val name = params["name"].toString()
        val id = redisTemplate.getUserId(session)
        val user = userRepository.findById(id).get()
        if (user.name == null) {
            response.status = StatusCode.Status401.statusCode
            return Result<String>(
                success = false,
                code = StatusCode.Status401.statusCode,
                msg = "用户不存在"
            )
        }
        asyncService.asyncTask {
            user.updateInfo(name)
            userRepository.save(user)
        }
        return Result(
            success = true,
            code = StatusCode.Status200.statusCode,
            msg = "OK"
        )
    }
}