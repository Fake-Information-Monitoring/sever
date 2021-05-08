package com.fake.information.sever.demo.Model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.*

@Entity
@Table(name = "model_data")
@JsonIgnoreProperties(value = ["model"])
class ModelData {
    @Id

    @Column(name = "model")
    var model: ByteArray? = null

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "model_id")
    var modelInfo: ModelInfo? = null
}