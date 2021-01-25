package com.fake.information.sever.demo.Model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import java.io.Serializable
import java.util.*
import javax.persistence.*
import kotlin.collections.ArrayList

@Entity
@Table(name="User",indexes =[ Index(columnList = "phone_number"),Index(columnList = "email") ] )
@JsonIgnoreProperties(value = ["password","lastActive","avatar"])
class User :Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    val id: Int = 0
    @Column(name = "name",nullable = false)
    var name: String? = null
    @Column(name = "phone_number",nullable = false)
    var phoneNumber: Long = 0
    @Column(name = "gender",nullable = true)
    var gender: String? = null
    @Column(name = "password",nullable = false)
    private var password: String? = null
    @Column(name = "email",nullable = false)
    var email: String? = null
    @Column(name = "updated_at",nullable = false)
    var update: Date? = null
    @Column(name = "last_actived_at",nullable = true)
    var lastActive: Date? = null
    @OneToMany(mappedBy = "user",cascade = [CascadeType.ALL],fetch = FetchType.EAGER)
    val commitList:MutableList<Commit> = LinkedList()
    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "user",cascade = [CascadeType.ALL],fetch = FetchType.EAGER)
    val keyList:MutableList<CDKey> = LinkedList()

    @Column(name = "avatar_id",nullable = true)
    var avatar: String? = null
    fun updateInfo(name:String,gender:String){
        this.name = name
        this.gender = gender
        update = Date()
    }
    fun getPassword(): String? {
        return password
    }
    fun setPassword(password:String){
        this.password = password
    }
//    @ExperimentalStdlibApi
//    fun getIndex():Map<String,Any?>{
//        return mapOf(
//                "name" to name,
//                "phoneNumber" to phoneNumber,
//                "uid" to id,
//                "gender" to gender,
//                "avatar" to avatar?.id,
//                "commit_num" to buildMap<Int,Any> {
//                    commitList.forEach {
//                        it.id to it.commitTime
//                    }
//                }
//        )
//    }
}
