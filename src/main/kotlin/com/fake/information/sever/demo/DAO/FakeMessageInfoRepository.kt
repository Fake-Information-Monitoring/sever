package com.fake.information.sever.demo.DAO

import com.fake.information.sever.demo.Model.FakeMessageInfo
import org.springframework.data.jpa.repository.JpaRepository
import java.io.Serializable

interface FakeMessageInfoRepository: JpaRepository<FakeMessageInfo, Int>, Serializable {
}