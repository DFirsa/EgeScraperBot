package ege.bot.config

import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.conf.Settings
import org.jooq.impl.DSL
import org.jooq.impl.DefaultConfiguration
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.sql.DriverManager

@Configuration
class DatasourceConfiguration {

    @Bean
    fun dslContext(
        @Value("\${database.jdbc-url}") jdbcUrl: String,
        @Value("\${database.password}") dbPassword: String,
        @Value("\${database.user}") dbUser: String
    ): DSLContext {
        return DSL.using(
            DefaultConfiguration()
                .set(DriverManager.getConnection(jdbcUrl, dbUser, dbPassword))
                .set(SQLDialect.POSTGRES)
                .set(
                    Settings()
                        .withRenderSchema(true)
                        .withFetchWarnings(false)
                )
        )
    }


}