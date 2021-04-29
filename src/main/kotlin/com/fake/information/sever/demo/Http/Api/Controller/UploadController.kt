package com.fake.information.sever.demo.Http.Api.Controller

import com.fake.information.sever.demo.Config.Redis.FakeNewsRedisTemplate
import com.fake.information.sever.demo.DTO.UserRepository
import com.fake.information.sever.demo.Http.Api.Response.Result
import com.fake.information.sever.demo.Http.Api.Response.StatusCode
import com.fake.information.sever.demo.Http.Api.Response.TokenType
import com.fake.information.sever.demo.Model.CDKey
import com.fake.information.sever.demo.Model.Commit
import com.fake.information.sever.demo.Until.AsyncTask.AsyncService
import com.fake.information.sever.demo.Until.JWT.TokenConfig
import com.fake.information.sever.demo.Until.OSS.OSSUpload
import com.fake.information.sever.demo.Until.UUID.UUID
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import java.util.*
import javax.servlet.http.HttpSession
import java.text.SimpleDateFormat


@RestController
@RequestMapping("/v1/upload")
@Api("上传文件管理")
class UploadController {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var redisTemplate: FakeNewsRedisTemplate

    @Autowired
    private lateinit var asyncService: AsyncService

    fun verifyToken(token: String): Boolean {
        val count = redisTemplate.getRedis(token + "nums").toString().toInt()
        redisTemplate.setRedis(token + "nums", count + 1)
        if (count >= TokenConfig.TOKEN_GET_COUNT) {
            return false
        }
        return true
    }

    @PostMapping("/uploadFile")
    @ApiOperation("上传文件")
    fun postCommitFile(
        @RequestHeader("appName") appName: String,
        @RequestHeader("type") type: String,
        @RequestBody file: MultipartFile,
        session: HttpSession
    ): Result<String> {
        if (appName.isEmpty()) {
            throw Exception("应用不能为空")
        }
        val id = redisTemplate.getUserId(session)
        val user = userRepository.getOne(id)
        val key = CDKey()
        key.key = UUID.getUuid()
        val filename = "${key.key}.txt"
        asyncService.asyncTask {
            OSSUpload.upload(file)
            println("上传成功！")
            val commit = Commit()
            commit.user = user
            key.name = appName
            user.keyList.add(key)
            key.user = user
            key.type = "Model"
            commit.indexOSSUrl = filename
            redisTemplate.setRedis(filename, TokenType.DIY_MODEL.toString())
            redisTemplate.setRedis(filename + "nums", 0)
            commit.commitTime = Date()
            user.commitList.add(commit)
            userRepository.save(user)
        }
        return Result(
            success = true,
            code = StatusCode.Status200.statusCode,
            msg = "正在上传，请等待三秒左右,若长时间未成功请重新提交",
            data = "https://${OSSUpload.bucketName}.${OSSUpload.endpoint}/" +
                    "${OSSUpload.fileHost}/${OSSUpload.dateStr}/${filename}"
        )
    }

    @PostMapping("/uploadImage")
    @ApiOperation("更新头像")
    fun postHeadImg(
        @RequestBody img: MultipartFile,
        session: HttpSession
    ): Result<String> {
        val id = redisTemplate.getUserId(session)
        val user = userRepository.getOne(id)
        user.avatar = OSSUpload.upload(img)
        userRepository.save(user)
        return Result<String>(
            success = true,
            code = StatusCode.Status200.statusCode,
            msg = "OK",
            data = user.avatar
        )
    }
}