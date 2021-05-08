package com.fake.information.sever.demo.Model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.*
import java.io.Serializable

@Entity
@Table(name = "certified_pool")
@JsonIgnoreProperties(value = ["user"])
class CertifiedPool : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    var id:Int = 0
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id")
    var user: User? = null

    @Column(name = "certified_type")
    var certifiedType: Int? = null

    @JvmName("setUser1")
    fun setUser(user: User) {
        this.user = user
        this.certifiedType = user.ceritifiedType
    }
}