package com.fake.information.sever.demo.Http.Controller.Api

import com.fake.information.sever.demo.DAO.AvatarRepository
import com.fake.information.sever.demo.DAO.UserRepository
import com.fake.information.sever.demo.Http.Controller.StatusCode
import com.fake.information.sever.demo.Model.Avatar
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import com.fake.information.sever.demo.Http.Response.Result
import java.util.*

@RestController
@RequestMapping("/v1/upload", method = [RequestMethod.POST, RequestMethod.GET])
class UploadController {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var avatarRepository: AvatarRepository

    @PostMapping("/uploadImage")
    fun postHeadImg(
            @RequestBody img: MultipartFile,
            @RequestHeader("id") id: Int
    ): Result<String> {
        val user = userRepository.getOne(id)
        if (user.avatar == null){
            user.avatar = Avatar()
            user.avatar!!.user = user
        }
        user.avatar!!.headImg = img.bytes
        userRepository.save(user)
        return Result<String>(
                success = true,
                code = StatusCode.Status_200.statusCode,
                msg = "OK"
        )
        //TODO:头像上传
    }

    fun inputStreamToFile(ins: InputStream, file: File?) {
        try {
            val os: OutputStream = FileOutputStream(file)
            var bytesRead = 0
            val buffer = ByteArray(81920)
            while (ins.read(buffer, 0, 81920).also { bytesRead = it } != -1) {
                os.write(buffer, 0, bytesRead)
            }
            os.close()
            ins.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}