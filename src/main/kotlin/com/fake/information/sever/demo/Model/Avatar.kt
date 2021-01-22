package com.fake.information.sever.demo.Model

import java.awt.Image
import java.awt.image.BufferedImage
import java.io.File
import javax.persistence.*

@Entity
@Table(name="avatar")
class Avatar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Int = 0

    @Column(name = "avatar_img")
    var headImg: File? = null
    @OneToOne(cascade = [CascadeType.ALL], optional = false)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    val index: User? = null
}