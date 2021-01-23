package com.fake.information.sever.demo.Controller

import com.fake.information.sever.demo.DAO.CommitRepository
import com.fake.information.sever.demo.DAO.UserRepository
import com.fake.information.sever.demo.Http.Controller.StatusCode
import com.fake.information.sever.demo.Model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import com.fake.information.sever.demo.Http.Response.Result
@RestController
@RequestMapping("/v1/getInfo",method = [ RequestMethod.GET])
class GetUserInfo {
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var commitRepository: CommitRepository
    @ExperimentalStdlibApi
    @PostMapping("/{user}")
    fun userInfo(@PathVariable user:Int): Any {
        try{
            return Result<User>(
                    success = true,
                    code = StatusCode.Status_200.statusCode,
                    msg = "success",
                    data = userRepository.findById(user).get()
            )
        }catch (e:NoSuchElementException){
            return Result<String>(
                    success = false,
                    code = StatusCode.Status_502.statusCode,
                    msg = e.toString()
            )
        }
    }
    @PostMapping("/{user}/avatar")
    fun userAvatar(@PathVariable user:Int): Any {
        try {
            return userRepository.getOne(user).avatar?.headImg!!
        }catch (e:NoSuchElementException){
            return Result<String>(
                    success = false,
                    code = StatusCode.Status_502.statusCode,
                    msg = e.toString()
            )
        }
    }
    @ExperimentalStdlibApi
    @PostMapping("/{user}/{commit}")
    fun getCommit(@PathVariable user:Int,@PathVariable commit:Int): Any {
        try{
                return commitRepository.getOne(commit).index?.index!!
        }catch (e:NoSuchElementException){
            return Result<String>(
                    success = false,
                    code = StatusCode.Status_502.statusCode,
                    msg = e.toString()
            )
        }
    }
}