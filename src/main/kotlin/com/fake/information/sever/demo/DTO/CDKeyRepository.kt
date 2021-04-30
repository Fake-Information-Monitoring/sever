package com.fake.information.sever.demo.DTO

import com.fake.information.sever.demo.Model.CDKey
import org.springframework.data.jpa.repository.JpaRepository

interface CDKeyRepository:JpaRepository<CDKey,Int> {
    fun findByKey(key: String):CDKey
}