package com.fake.information.sever.demo.Model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.*

@Entity
@Table(name = "person_certified")
class PersonCertified {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Int = 0

    @Column(name = "name", nullable = false)
    var name: String? = null

    @Column(name = "card_id", nullable = false)
    var cardId: String? = null

    @Column(name = "work", nullable = false)
    var work: String? = null

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id")
    var user: User? = null
}