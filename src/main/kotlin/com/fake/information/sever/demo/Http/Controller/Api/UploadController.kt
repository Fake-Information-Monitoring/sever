package com.fake.information.sever.demo.Http.Controller.Api

import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/upload", method = [RequestMethod.POST, RequestMethod.GET])
class UploadController {
    @PutMapping("/uploadImage")
    fun putHeadImg() {
        //TODO:头像上传
    }
}