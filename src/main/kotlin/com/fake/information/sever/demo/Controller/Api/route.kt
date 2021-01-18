package com.fake.information.sever.demo.Controller.Api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class ProductServiceController{
    @RequestMapping(value = ["/products"])
    fun getProducts(): ResponseEntity<Any?>? {
        return null
    }

}


//更多请阅读：https://www.yiibai.com/spring-boot/spring_boot_building_restful_web_services.html#article-start

