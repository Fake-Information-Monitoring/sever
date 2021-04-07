package com.fake.information.sever.demo.Controller

import com.fake.information.sever.demo.DTO.UserRepository
import com.fake.information.sever.demo.Http.Response.StatusCode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import com.fake.information.sever.demo.Http.Response.Result
import com.fake.information.sever.demo.Until.AsyncTask.AsyncService
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/v1/update", method = [RequestMethod.PUT])
class UpdateController {
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var asyncService: AsyncService
    @ExperimentalStdlibApi
    @PutMapping("/update")
    fun putUpdate(
               @RequestHeader("name") name: String,
               @RequestHeader("id") id:Int,
               response: HttpServletResponse
    ): Result<String> {
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
        return Result<String>(
                success = true,
                code = StatusCode.Status200.statusCode,
                msg = "OK"
        )
    }
}