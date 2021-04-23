import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.4.2"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.4.21"
    kotlin("plugin.spring") version "1.4.21"
}

group = "com.fake.information.sever"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    maven("https://maven.aliyun.com/nexus/content/groups/public/")
    maven("https://maven.aliyun.com/repository/spring")
    maven("https://maven.aliyun.com/repository/spring-plugin")
    maven("https://jitpack.io")
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
//    implementation("org.springframework.boot:spring-boot-starter-security")//spring security
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.8.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.auth0:java-jwt:3.3.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    // implementation("org.springframework.boot:spring-boot-starter-activemq:1.5.0.RELEASE")//ActiveMQ
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")//Spring Jdbc
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")//Spring Jpa
    implementation("org.springframework.boot:spring-boot-starter-mail")//Spring Email
    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.boot:spring-boot-starter-freemarker")
    implementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
    runtimeOnly("mysql:mysql-connector-java")//Mysql Driver
    implementation("io.jsonwebtoken:jjwt-api:0.11.2") // jjwt
    implementation("io.jsonwebtoken:jjwt-impl:0.11.2") // jjwt
    implementation("io.jsonwebtoken:jjwt-gson:0.11.2")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.2") // or 'io.jsonwebtoken:jjwt-gson:0.11.2' for gson
    implementation("io.lettuce:lettuce-core:6.0.2.RELEASE") //lettuce
    implementation("com.google.code.gson:gson:2.8.6")//gson
    implementation("com.aliyun.oss:aliyun-sdk-oss:3.10.2")//oss
    implementation("javax.xml.bind:jaxb-api:2.3.1")//oss依赖
    implementation("javax.activation:activation:1.1.1")//oss依赖
    implementation("org.glassfish.jaxb:jaxb-runtime:2.3.3")//oss依赖
    implementation("cn.hutool:hutool-all:4.5.15") // 图片验证码库
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")


}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
