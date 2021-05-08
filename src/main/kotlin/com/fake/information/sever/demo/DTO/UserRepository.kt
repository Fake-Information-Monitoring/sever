package com.fake.information.sever.demo.DTO

import com.fake.information.sever.demo.Model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.io.Serializable

@Repository
interface UserRepository:JpaRepository<User,Int>, Serializable {
    fun findByPhoneNumber(phoneNumber: String):User?
    fun findByEmail(email: String):User?
    fun findByName(email: String):User?
}