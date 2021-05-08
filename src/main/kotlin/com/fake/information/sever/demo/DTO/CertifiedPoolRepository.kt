package com.fake.information.sever.demo.DTO

import com.fake.information.sever.demo.Model.CertifiedPool
import org.springframework.data.jpa.repository.JpaRepository
import java.io.Serializable

interface CertifiedPoolRepository : JpaRepository<CertifiedPool, Int>, Serializable {
}