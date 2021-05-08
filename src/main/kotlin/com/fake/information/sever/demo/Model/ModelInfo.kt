package com.fake.information.sever.demo.Model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.web.multipart.MultipartFile
import java.io.File
import javax.persistence.*

@Entity
@Table(name = "model_info")

@JsonIgnoreProperties(value = ["model","key"])
class ModelInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "key_id")
    var key: CDKey? = null

    @OneToOne(cascade = [CascadeType.ALL], mappedBy = "modelInfo")
    var modelId: ModelData? = null

    @Column(name = "type")
    var modelName: String? = null

    @Column(name = "status")
    var trainStatus: Int = -1

    fun setModel(model:ByteArray){
        this.modelId = ModelData()
        this.modelId!!.model = model
    }
}