package com.fake.information.sever.demo.Http.Controller

import com.fake.information.sever.demo.Http.Response.Result
import com.fake.information.sever.demo.Http.Upload.OSSUpload
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream

@RestController
@RequestMapping("/")
class UploadControllers {
    @PostMapping("/upload")
    fun uploadBlog(@RequestParam("file") file: MultipartFile?): Any {
        try {

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return Result<String>(
                success = false,
                code = StatusCode.Status_502.statusCode,
                msg = "failed",
        )
    }
}