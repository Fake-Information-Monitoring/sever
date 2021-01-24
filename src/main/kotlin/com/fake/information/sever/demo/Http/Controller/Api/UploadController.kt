package com.fake.information.sever.demo.Http.Controller.Api

import com.fake.information.sever.demo.DAO.AvatarRepository
import com.fake.information.sever.demo.DAO.CommitRepository
import com.fake.information.sever.demo.DAO.UserRepository
import com.fake.information.sever.demo.Http.Controller.StatusCode
import com.fake.information.sever.demo.Model.Avatar
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import com.fake.information.sever.demo.Http.Response.Result
import com.fake.information.sever.demo.Http.Upload.OSSUpload
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

    @Autowired
    private lateinit var avatarRepository: AvatarRepository

    @PostMapping("/uploadFile")
    fun postCommitFile(
            @RequestBody file: MultipartFile,
            @RequestHeader("id") id: Int
    ): Result<String> {
        val user = userRepository.getOne(id)
        val commit = Commit()
        commit.user = user
        val filename = file.originalFilename
        if ("" != filename?.trim { it <= ' ' }) {
            val newFile = File(filename)
            val os = FileOutputStream(newFile)
            os.write(file.bytes)
            os.close()
            file.transferTo(newFile)
            //上传到OSS
            val uploadUrl: String? = OSSUpload.upload(newFile)
            commit.indexOSSUrl = uploadUrl
            commit.commitTime = Date()
            user.commitList.add(commit)
            userRepository.save(user)
            return Result<String>(
                    success = true,
                    code = StatusCode.Status_200.statusCode,
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
        if (user.avatar == null) {
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
    }
}