package com.fake.information.sever.demo.Model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.*

@Entity
@Table(name="key")
@JsonIgnoreProperties(value = ["user"])
class CDKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    val id: Int = 0
    @Column(name = "key_str",nullable = false)
    var key:String? = null
    @Column(name = "name",nullable = false)
    var name:String? = null
    @ManyToOne(targetEntity = User::class,cascade = [CascadeType.ALL],optional = false)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    var user:User? = null
    override fun toString(): String {
        return this.key!!
    }
    @Column(name = "type",nullable = false)
    var type: String? = null

    @OneToOne(cascade = [CascadeType.ALL], mappedBy = "key")
    var model: DIYModel? = null
}