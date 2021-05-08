package com.fake.information.sever.demo.Model

import javax.persistence.*


@Entity
@Table(name = "enterprise_certified")
class CorporateCertified {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null

    @Column(name = "name", nullable = false)
    var name: String? = null

    @Column(name = "license", nullable = false)
    var license: String? = null

    @Column(name = "phone", nullable = false)
    var phone: String? = null

    @Column(name = "email", nullable = false)
    var email: String? = null

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id")
    var user: User? = null
}