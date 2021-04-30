package com.fake.information.sever.demo.Model

import org.springframework.web.multipart.MultipartFile
import java.io.File
import javax.persistence.*

@Entity
@Table(name = "model")
class DIYModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "key_id")
    var key: CDKey? = null
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "model")
    var model: ByteArray? = null

    @Column(name = "type")
    var type: String? = null
}