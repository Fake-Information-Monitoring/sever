package com.fake.information.sever.demo.DAO

import com.fake.information.sever.demo.Model.Commit
import org.springframework.data.domain.Example
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.io.Serializable
import java.util.*

@Repository
interface CommitRepository:JpaRepository<Commit,Int>, Serializable {

}