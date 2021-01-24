package com.fake.information.sever.demo.Model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.File
import javax.persistence.*

@Entity
@Table(name="avatar")
@JsonIgnoreProperties(value = ["headImg"])
class Avatar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Int = 0
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "avatar_img")
    var headImg: ByteArray? = null
    @OneToOne(targetEntity = User::class, optional = false)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    var user: User? = null
}