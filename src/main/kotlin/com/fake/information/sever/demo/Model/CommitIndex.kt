package com.fake.information.sever.demo.Model

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "commit_index")
class CommitIndex: Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Int = 0
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "index", nullable = false)
    var index: ByteArray? = null
    @OneToOne(mappedBy = "index",cascade = [CascadeType.MERGE, CascadeType.REFRESH],optional = false)
    var commit: Commit? = null
}