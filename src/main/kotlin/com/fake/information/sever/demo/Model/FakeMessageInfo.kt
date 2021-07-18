package com.fake.information.sever.demo.Model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "fake_message_info")
@JsonIgnoreProperties(value = ["cdKey"])
class FakeMessageInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = 0

    @ManyToOne(targetEntity = CDKey::class,cascade = [CascadeType.ALL],optional = false)
    @JoinColumn(name = "key_id",referencedColumnName = "id")
    var cdKey:CDKey? = null

    @Column(name = "worse_account")
    var account:String? = null
    @Column(name = "worse_name")
    var name:String? = null
    @OptIn(ExperimentalStdlibApi::class)
    fun toJsonString(): String {
        return "{\"account\":\"$account\"," +
                "\"name\":\"$name\"," +
                "\"info\":\"$info\"," +
                "\"type\":\"$type\"," +
                "\"time\":\"$time\"," +
                "\"appId\":\"${cdKey?.id}\"," +
                "\"words\":\"${words}\"}"
    }

    @Column(name = "worse_info")
    var info:String? = null
    @Column(name = "worse_type")
    var type:String? = null
    @Column(name = "time")
    var time: Date? = null
    @Column(name = "words",nullable = true)
    var words:String? = null
}