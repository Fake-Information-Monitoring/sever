package com.fake.information.sever.demo.Http.Api.Controller

import com.fake.information.sever.demo.DTO.CommitRepository
import com.fake.information.sever.demo.DTO.UserRepository
import com.fake.information.sever.demo.Http.Api.Response.StatusCode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import com.fake.information.sever.demo.Http.Api.Response.Result
import com.fake.information.sever.demo.Until.OSS.OSSUpload
import com.fake.information.sever.demo.Model.Commit
import com.fake.information.sever.demo.Redis.FakeNewsRedisTemplate
import com.fake.information.sever.demo.Until.AsyncTask.AsyncService
import com.fake.information.sever.demo.Until.JWT.TokenConfig
import java.io.File
import java.io.FileOutputStream
import java.util.*
import javax.servlet.http.HttpSession

@RestController
@RequestMapping("/v1/upload", method = [RequestMethod.POST, RequestMethod.GET])
class UploadController {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var commitRepository: CommitRepository
    @Autowired
    private lateinit var asyncService: AsyncService
    @Autowired
    private lateinit var redisTemplate: FakeNewsRedisTemplate

    fun multipartFileToFile(multipartFile: MultipartFile, fileName: String): File {
        val newFile = File(fileName)
        val os = FileOutputStream(newFile)
        os.write(multipartFile.bytes)
        os.close()
        multipartFile.transferTo(newFile)
        return newFile
    }
    fun verifyToken(token: String): Boolean {
        val count = redisTemplate.getRedis(token).toString().toInt()
        redisTemplate.setRedis(token, count + 1)
        if (count >= TokenConfig.TOKEN_GET_COUNT) {
            return false
        }
        return true
    }
    @PostMapping("/uploadFile")
    fun postCommitFile(
        @RequestBody file: MultipartFile,
        @RequestHeader("token") token: String,
        session: HttpSession
    ): Result<String> {
        if (!verifyToken(token)) {
            return Result(
                success = false,
                code = StatusCode.Status401.statusCode,
                msg = "已失效"
            )
        }
        val id = redisTemplate.getUserId(session)
        val user = userRepository.getOne(id)
        val commit = Commit()
        commit.user = user
        val filename = file.originalFilename
        if ("" != filename?.trim { it <= ' ' }) {
            val newFile = multipartFileToFile(file, filename!!)
            //上传到OSS
            val uploadUrl: String? = OSSUpload.upload(newFile)
            commit.indexOSSUrl = uploadUrl
            commit.commitTime = Date()
            user.commitList.add(commit)
            userRepository.save(user)
            return Result(
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
        session: HttpSession
    ): Result<String> {
        val id = redisTemplate.getUserId(session)
        val user = userRepository.getOne(id)
        user.avatar = OSSUpload.upload(
            multipartFileToFile(img, img.originalFilename!!)
        )
        userRepository.save(user)
        return Result<String>(
            success = true,
            code = StatusCode.Status200.statusCode,
            msg = "OK",
            data = user.avatar
        )
    }
}