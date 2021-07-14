package com.fake.information.sever.demo.Model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*
import javax.persistence.*

@Entity
@Table(name="key",indexes = [Index(columnList = "key_str")])
@JsonIgnoreProperties(value = ["user","fakeMessageInfoList"])
class CDKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    val id: Int = 0
    @Column(name = "key_str",nullable = false)
    var key:String? = null
    @Column(name = "name",nullable = false)
    var keyName:String? = null
    @ManyToOne(targetEntity = User::class,cascade = [CascadeType.ALL],optional = false)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    var user:User? = null


    @OneToMany(mappedBy = "cdKey", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var fakeMessageInfoList:MutableList<FakeMessageInfo>? = LinkedList()

    override fun toString(): String {
        return this.key!!
    }
    @Column(name = "type",nullable = false)
    var keyType: String? = null

    @OneToOne(cascade = [CascadeType.ALL], mappedBy = "key")
    var model: ModelInfo? = null
    
    @Column(name = "enterprise_name",nullable = true)
    var enterpriseName:String? = null
}