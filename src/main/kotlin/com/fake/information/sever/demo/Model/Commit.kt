package com.fake.information.sever.demo.Model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@Table(name="commit")
@JsonIgnoreProperties(value = ["user"])
class Commit: Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    var id: Int = 0
    @ManyToOne(targetEntity = User::class,cascade = [CascadeType.ALL],optional = false)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    var user: User? = null
    @Column(name = "commit_at",nullable = false)
    var commitTime: Date? = null
    @Column(name = "index",nullable = false)
    var indexOSSUrl: String? = null
}