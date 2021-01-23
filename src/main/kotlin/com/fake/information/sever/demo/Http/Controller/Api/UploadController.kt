package com.fake.information.sever.demo.Http.Controller.Api

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/upload", method = [RequestMethod.POST, RequestMethod.GET])
class UploadController {
    @PostMapping("/uploadImage")
    fun putHeadImg() {
        //TODO:头像上传
    }
}