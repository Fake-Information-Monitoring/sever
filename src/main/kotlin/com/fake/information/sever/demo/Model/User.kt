package com.fake.information.sever.demo.Model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption
import java.io.Serializable
import java.util.*
import javax.persistence.*
import kotlin.collections.ArrayList

@Entity
@Table(name = "User", indexes = [Index(columnList = "phone_number"), Index(columnList = "email")])
@JsonIgnoreProperties(value = ["password", "lastActive"])
class User : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Int = 0

    @Column(name = "name", nullable = false)
    var name: String? = null

    @Column(name = "phone_number", nullable = false)
    var phoneNumber: String? = null

    @Column(name = "password", nullable = false)
    private var password: String? = null

    @Column(name = "email", nullable = false)
    var email: String? = null

    @Column(name = "updated_at", nullable = false)
    var update: Date? = null

    @Column(name = "last_actived_at", nullable = true)
    var lastActive: Date? = null


    @Column(name = "ceritified_id", nullable = true)
    var ceritifiedID: Int? = -1

    @Column(name = "ceritified_type", nullable = true)
    var ceritifiedType: Int? = null

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    val keyList: MutableList<CDKey> = LinkedList()
    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    val commitList: MutableList<Commit> = LinkedList()
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var fakeMessageInfoList:MutableList<FakeMessageInfo>? = LinkedList()

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "ceritified_admin",nullable = true)
    var admin:Admin?=null

    @Column(name = "avatar_id", nullable = true)
    var avatar: String? = null
    fun updateInfo(name: String) {
        this.name = name
        update = Date()
    }

    fun getPassword(): String? {
        return password
    }

    fun setPassword(password: String) {
        this.password = password
    }
}
