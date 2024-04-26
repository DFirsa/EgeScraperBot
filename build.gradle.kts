import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jooq.meta.jaxb.Logging

val jooqVersion: String by project
val jdbcUrl: String = System.getenv("DB_JDBC") ?: "jdbc:postgresql://localhost:54321/postgres"
val dbPassword: String = System.getenv("DB_PWD") ?: "postgres"
val dbUser: String = System.getenv("DB_USR") ?: "postgres"

plugins {
    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.spring") version "1.9.22"

    id("nu.studer.jooq") version "9.0"
}
rm
group = "ege.bot"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("io.github.kotlin-telegram-bot.kotlin-telegram-bot:telegram:6.1.0")
    implementation("org.jsoup:jsoup:1.17.2")
    implementation("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    jooqGenerator("org.postgresql:postgresql")
}

jooq {
    version.set("3.18.14")
    configurations {
        create("main") {
            generateSchemaSourceOnCompilation.set(true)

            jooqConfiguration.apply {
                logging = Logging.WARN

                generator.apply {
                    name = "org.jooq.codegen.KotlinGenerator"

                    jdbc.apply {
                        driver = "org.postgresql.Driver"
                        url = jdbcUrl
                        user = dbUser
                        password = dbPassword
                    }
                    target.apply {
                        packageName = "ege.bot.service.codegen.models"
                        directory = "src/generated-src/jooq/main"
                    }
                    database.apply {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = "results"
                    }
                }
            }
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "21"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.bootJar {
    archiveFileName.set("app.jar")
}

