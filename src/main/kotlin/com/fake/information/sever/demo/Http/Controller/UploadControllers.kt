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
            if (null != file) {
                val filename = file.originalFilename
                if ("" != filename?.trim { it <= ' ' }) {
                    val newFile = File(filename)
                    val os = FileOutputStream(newFile)
                    os.write(file.bytes)
                    os.close()
                    file.transferTo(newFile)
                    //上传到OSS
                    val uploadUrl: String? = OSSUpload.upload(newFile)
                    return Result<String>(
                            success = true,
                            code = StatusCode.Status_200.statusCode,
                            msg = "success",
                            data = uploadUrl
                    )
                }
            }
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