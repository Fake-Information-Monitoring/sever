package com.fake.information.sever.demo.DAO

import com.fake.information.sever.demo.Model.CorporateCertified
import org.springframework.data.jpa.repository.JpaRepository
import java.io.Serializable
interface CorporateCertifiedRepository: JpaRepository<CorporateCertified, Int>, Serializable {
}