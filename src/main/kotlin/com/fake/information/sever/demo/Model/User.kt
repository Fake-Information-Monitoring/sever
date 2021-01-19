package com.fake.information.sever.demo.Model

import java.io.Serializable
import java.util.*
import javax.persistence.*
import kotlin.collections.ArrayList

@Entity
@Table(name="User")
class User :Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    val id: Int = 0
    @Column(name = "name",nullable = false)
    var name: String? = null
    @Column(name = "avatar_id",nullable = true)
    val avatarId: Int = 0
    @Column(name = "phone_number",nullable = false)
    var phoneNumber: Long = 0
    @Column(name = "gender",nullable = true)
    var gender: String? = null
    @Column(name = "password",nullable = false)
    var password: String? = null
    @Column(name = "email",nullable = false)
    var email: String? = null
    @Column(name = "updated_at",nullable = false)
    var update: Date? = null
    @Column(name = "last_actived_at",nullable = true)
    val lastActive: Date? = null
    @OneToMany(mappedBy = "user",cascade = [CascadeType.ALL],fetch = FetchType.EAGER)
    val commitList:List<Commit> = ArrayList()
}
