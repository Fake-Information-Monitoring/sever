package com.fake.information.sever.demo.Model

import java.io.File
import javax.persistence.*
import java.io.Serializable
@Entity
@Table(name = "commit_index")
class CommitIndex: Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    var id: Int = 0
    @Column(name = "index",nullable = false)
    val index: File? = null
    @OneToOne(targetEntity = Commit::class,cascade = [CascadeType.ALL],optional = false)
    @JoinColumn(name = "commit_id",nullable = false,referencedColumnName = "id")
    val commit: Commit? = null
}