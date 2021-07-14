package com.fake.information.sever.demo.Model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.*

@Entity
@Table(name = "fake_message_info")
@JsonIgnoreProperties(value = ["user"])
class FakeMessageInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = 0
    @ManyToOne(targetEntity = User::class,cascade = [CascadeType.ALL],optional = false)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    var user:User? = null
    @Column(name = "worse_account")
    var account:String? = null
    @Column(name = "worse_name")
    var name:String? = null
    @Column(name = "worse_info")
    var info:String? = null
    @Column(name = "worse_type")
    var type:String? = null
}