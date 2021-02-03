package com.fake.information.sever.demo.Controller

import com.fake.information.sever.demo.DAO.CommitRepository
import com.fake.information.sever.demo.DAO.UserRepository
import com.fake.information.sever.demo.Http.Controller.StatusCode
import com.fake.information.sever.demo.Model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import com.fake.information.sever.demo.Http.Response.Result
import javax.servlet.http.HttpSession

@RestController
@RequestMapping("/v1/getInfo", method = [RequestMethod.GET])
class GetUserInfo {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var commitRepository: CommitRepository

    @ExperimentalStdlibApi
    @GetMapping("/{user}")
    fun getUserInfo(
            @PathVariable user: Int,
            session: HttpSession
    ): Any {

        return try {
            if (session.getAttribute(user.toString()) != StatusCode.Status_200.statusCode) {
                throw IllegalAccessException("您没有权限")
            }
            Result<User>(
                    success = true,
                    code = StatusCode.Status_200.statusCode,
                    msg = "success",
                    data = userRepository.findById(user).get()
            )

        } catch (e: NoSuchElementException) {
            Result<String>(
                    success = false,
                    code = StatusCode.Status_502.statusCode,
                    msg = e.toString()
            )
        }
    }

    @GetMapping("/{user}/avatar")
    fun getUserAvatar(
            @PathVariable user: Int,
            session: HttpSession
    ): Any {
        return try {
            if (session.getAttribute(user.toString()) != StatusCode.Status_200.statusCode) {
                throw IllegalAccessException("您没有权限")
            }
            Result<String>(
                    success = true,
                    code = StatusCode.Status_200.statusCode,
                    msg = "success",
                    data = userRepository.getOne(user).avatar!!
            )
        } catch (e: NoSuchElementException) {
            Result<String>(
                    success = false,
                    code = StatusCode.Status_502.statusCode,
                    msg = e.toString()
            )
        }
    }

    @ExperimentalStdlibApi
    @GetMapping("/{user}/{commit}")
    fun getCommit(@PathVariable user: Int,
                  @PathVariable commit: Int,
                  session: HttpSession
    ): Any {
        return try {
            if (session.getAttribute(user.toString()) != StatusCode.Status_200.statusCode) {
                throw IllegalAccessException("您没有权限")
            }
            commitRepository.getOne(commit).indexOSSUrl!!
        } catch (e: NoSuchElementException) {
            Result<String>(
                    success = false,
                    code = StatusCode.Status_502.statusCode,
                    msg = e.toString()
            )
        }
    }
}