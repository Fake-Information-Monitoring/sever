package com.fake.information.sever.demo.Http.Controller.Api

import com.fake.information.sever.demo.Http.Controller.Api.Until.Sobel
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/", method = [RequestMethod.POST])
class ImageEdgeDetection {
    @PostMapping("/ImageEdge", produces = ["image/png"])
    fun postImgFile(@RequestBody file: MultipartFile): ByteArray {
        val img = ByteArrayInputStream(file.bytes)
        val imageIO = ImageIO.read(img)?:throw IllegalAccessException("图片格式错误")
        val buffer = Sobel().edgeExtract2(imageIO)
        val byteArrayOutputStream = ByteArrayOutputStream()
        ImageIO.write(buffer,"png",byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }
    @PostMapping("/ImageEdgeByte")
    fun postImgByte(@RequestBody byte: ByteArray,request: HttpServletRequest): ByteArray {
        val img = ByteArrayInputStream(byte)
        val bufferedImage = ImageIO.read(img)?:throw IllegalAccessException("图片格式错误")
        val buffer = Sobel().edgeExtract2(bufferedImage)
        val byteArrayOutputStream = ByteArrayOutputStream()
        ImageIO.write(buffer,"png",byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }
}