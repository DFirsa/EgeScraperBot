package ege.bot.service.scraper

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ScrapperConfiguration(
    @Value("\${results-source.spb-ege.selector.subject}")
    private val subjectSelector: String,
    @Value("\${results-source.spb-ege.selector.result}")
    private val examSelector: String,
    @Value("\${results-source.spb-ege.result-parser.exam-success}")
    private val examSuccessResult: String
) {
    @Bean
    fun spbEgeScraper(): Scraper {
        return DefaultClassBasedScraper(
            subjectSelector,
            examSelector,
            examSuccessResult
        )
    }
}