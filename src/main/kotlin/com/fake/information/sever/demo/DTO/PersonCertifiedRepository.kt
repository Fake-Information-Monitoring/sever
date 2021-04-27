package com.fake.information.sever.demo.DTO

import com.fake.information.sever.demo.Model.PersonCertified
import com.fake.information.sever.demo.Model.User
import org.springframework.data.jpa.repository.JpaRepository
import java.io.Serializable

interface PersonCertifiedRepository: JpaRepository<PersonCertified, Int>, Serializable {
}