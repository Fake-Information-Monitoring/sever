package com.fake.information.sever.demo.DAO

import com.fake.information.sever.demo.Model.PersonCertified
import org.springframework.data.jpa.repository.JpaRepository
import java.io.Serializable

interface PersonCertifiedRepository: JpaRepository<PersonCertified, Int>, Serializable {
}