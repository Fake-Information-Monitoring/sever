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
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    // implementation("org.springframework.boot:spring-boot-starter-activemq:1.5.0.RELEASE")//ActiveMQ
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")//Spring Jdbc
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")//Spring Jpa
    runtimeOnly("mysql:mysql-connector-java")//Mysql Driver
    implementation("com.github.PhilJay:JWT:1.1.5")//jwt in kotlin //https://github.com/PhilJay/JWT
    implementation("org.springframework.session:spring-session-data-redis")//session
    implementation("org.springframework.boot:spring-boot-starter-data-redis")//redis
    implementation("io.lettuce:lettuce-core:6.0.2.RELEASE") //lettuce
    implementation("com.google.code.gson:gson:2.8.6")//gson
    implementation("com.aliyun.oss:aliyun-sdk-oss:3.10.2")//oss
    implementation("javax.xml.bind:jaxb-api:2.3.1")//oss依赖
    implementation("javax.activation:activation:1.1.1")//oss依赖
    implementation("org.glassfish.jaxb:jaxb-runtime:2.3.3")//oss依赖
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
