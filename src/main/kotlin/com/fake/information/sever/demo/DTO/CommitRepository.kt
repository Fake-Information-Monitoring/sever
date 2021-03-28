package com.fake.information.sever.demo.DTO

import com.fake.information.sever.demo.Model.Commit
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.io.Serializable

@Repository
interface CommitRepository:JpaRepository<Commit,Int>, Serializable {

}