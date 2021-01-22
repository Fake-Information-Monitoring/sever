package com.fake.information.sever.demo.Controller

import com.fake.information.sever.demo.Controller.tools.BuildError
import com.fake.information.sever.demo.DAO.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/update", method = [RequestMethod.POST])
class UpdateController {
    @Autowired
    private lateinit var userRepository: UserRepository

    @PutMapping("/uploadImage")
    fun putHeadImg() {
         //TODO:头像上传
    }

    @ExperimentalStdlibApi
    @PostMapping("/update")
    fun update(@RequestHeader("sex") sex: String,
               @RequestHeader("name") name: String,
               @RequestHeader("id") id:Int): Map<String, Any> {
        val user = userRepository.findById(id).get()
        if (user.name == null) {
            return BuildError.buildErrorInfo("用户不存在")
        }
        user.updateInfo(name, sex)
        userRepository.save(user)
        return mapOf<String, Any>(
                "status" to "OK"
        )
    }
}