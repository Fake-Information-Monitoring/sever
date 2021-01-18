package com.fake.information.sever.demo.Controller.Api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/login")
class ProductServiceController{
    @RequestMapping(value = ["/products"])
    fun getProducts(): ResponseEntity<Any?>? {
        return null
    }
}