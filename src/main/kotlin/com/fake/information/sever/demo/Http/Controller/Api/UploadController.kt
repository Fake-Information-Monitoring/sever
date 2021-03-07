package com.fake.information.sever.demo.Http.Controller.Api

import com.fake.information.sever.demo.DAO.CommitRepository
import com.fake.information.sever.demo.DAO.UserRepository
import com.fake.information.sever.demo.Http.Controller.StatusCode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import com.fake.information.sever.demo.Http.Response.Result
import com.fake.information.sever.demo.Http.Upload.OSSUpload
import com.fake.information.sever.demo.JWT.JWTManage
import com.fake.information.sever.demo.Model.Commit
import java.io.File
import java.io.FileOutputStream
import java.util.*

@RestController
@RequestMapping("/v1/upload", method = [RequestMethod.POST, RequestMethod.GET])
class UploadController {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var commitRepository: CommitRepository


    fun multipartFileToFile(multipartFile: MultipartFile,fileName:String):File{
        val newFile = File(fileName)
        val os = FileOutputStream(newFile)
        os.write(multipartFile.bytes)
        os.close()
        multipartFile.transferTo(newFile)
        return newFile
    }
    @PostMapping("/uploadFile")
    fun postCommitFile(
            @RequestBody file: MultipartFile,
            @RequestHeader("id") id: Int,
            @RequestHeader("token") token:String
    ): Result<String> {
        if (JWTManage.verifyToken(token) != JWTManage.TokenVerifyCode.Success.verifyCode) {
            return Result<String>(
                    success = false,
                    code = StatusCode.Status401.statusCode,
                    msg = "Token无效"
            )
        }
        val user = userRepository.getOne(id)
        val commit = Commit()
        commit.user = user
        val filename = file.originalFilename
        if ("" != filename?.trim { it <= ' ' }) {
            val newFile = multipartFileToFile(file,filename!!)
            //上传到OSS
            val uploadUrl: String? = OSSUpload.upload(newFile)
            commit.indexOSSUrl = uploadUrl
            commit.commitTime = Date()
            user.commitList.add(commit)
            userRepository.save(user)
            return Result<String>(
                    success = true,
                    code = StatusCode.Status200.statusCode,
                    msg = "success",
                    data = uploadUrl
            )
        }
        throw IllegalAccessError("文件名不合法！")
    }

    @PostMapping("/uploadImage")
    fun postHeadImg(
            @RequestBody img: MultipartFile,
            @RequestHeader("id") id: Int
    ): Result<String> {
        val user = userRepository.getOne(id)
        user.avatar =  OSSUpload.upload(multipartFileToFile(img,img.originalFilename!!))
        userRepository.save(user)
        return Result<String>(
                success = true,
                code = StatusCode.Status200.statusCode,
                msg = "OK",
                data = user.avatar
        )
    }
}