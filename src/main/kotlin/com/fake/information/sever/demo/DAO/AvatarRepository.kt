package com.fake.information.sever.demo.DAO

import com.fake.information.sever.demo.Model.Avatar
import org.springframework.data.jpa.repository.JpaRepository
import java.io.Serializable

interface AvatarRepository: JpaRepository<Avatar, Int>, Serializable {
}