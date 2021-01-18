package com.fake.information.sever.demo.DAO

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

@Configuration
@EnableJpaRepositories
@EnableTransactionManagement
class DB {
//    @Bean
//    fun dataSource(): EmbeddedDatabase {
//        return EmbeddedDatabaseBuilder()
//                .setType(EmbeddedDatabaseType.HSQL).build()
//    }
}