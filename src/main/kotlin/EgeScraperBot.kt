package ege.bot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class EgeScraperBot

fun main(args: Array<String>) {
    runApplication<EgeScraperBot>(*args)
}