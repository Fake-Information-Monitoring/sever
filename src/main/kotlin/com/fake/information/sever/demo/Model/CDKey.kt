package com.fake.information.sever.demo.Model

import javax.persistence.*

@Entity
@Table(name="key")
class CDKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    val id: Int = 0
    @Column(name = "key_str",nullable = false)
    val key:String? = null
    @ManyToOne(targetEntity = User::class,cascade = [CascadeType.ALL],optional = false)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    val user:User? = null
}