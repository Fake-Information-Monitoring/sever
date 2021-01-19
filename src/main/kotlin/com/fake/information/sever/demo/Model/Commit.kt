package com.fake.information.sever.demo.Model

import java.util.*
import javax.persistence.*

@Entity
@Table(name="commit")
class Commit {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    var id: Int = 0
    @ManyToOne(cascade = [CascadeType.ALL],optional = false)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    val user: User? = null
    @Column(name = "commit_at",nullable = false)
    val commitTime: Date? = null
    @OneToOne(cascade = [CascadeType.ALL],optional = false)
    @JoinColumn(name = "index_id",referencedColumnName = "id")
    val index: CommitIndex? = null
}