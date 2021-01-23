package com.fake.information.sever.demo.Controller

import com.fake.information.sever.demo.DAO.UserRepository
import com.fake.information.sever.demo.Http.Controller.StatusCode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import com.fake.information.sever.demo.Http.Response.Result
@RestController
@RequestMapping("/v1/update", method = [RequestMethod.PUT])
class UpdateController {
    @Autowired
    private lateinit var userRepository: UserRepository

    @ExperimentalStdlibApi
    @PutMapping("/update")
    fun update(@RequestHeader("sex") sex: String,
               @RequestHeader("name") name: String,
               @RequestHeader("id") id:Int): Result<String> {
        val user = userRepository.findById(id).get()
        if (user.name == null) {
            return Result<String>(
                    success = false,
                    code = StatusCode.Status_401.statusCode,
                    msg = "用户不存在"
            )
        }
        user.updateInfo(name, sex)
        userRepository.save(user)
        return Result<String>(
                success = true,
                code = StatusCode.Status_200.statusCode,
                msg = "OK"
        )
    }
}