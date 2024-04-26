package ege.bot.controller

import ege.bot.model.ExamType
import ege.bot.model.IdentificationInfo
import ege.bot.service.bot_command.TelegramBotService
import ege.bot.service.codegen.models.tables.references.CHAT
import ege.bot.service.data_getter.SpbEgeDataGetter
import ege.bot.service.scraper.Scraper
import org.jooq.DSLContext
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Year

@RestController
class Controller(
    val spbEgeDataGetter: SpbEgeDataGetter,
    val spbEgeScraper: Scraper,
    val telegramBot: TelegramBotService,
    val dslContext: DSLContext
) {

    @GetMapping("/test")
    fun getData(): String {
        val doc = spbEgeDataGetter.getPageData(
            ExamType.OGE,
            Year.of(2016),
            IdentificationInfo("4014", "014626")
        )
        return spbEgeScraper.getResults(doc).joinToString(" ")
    }

    @PostMapping("/test/bot")
    fun test2(
        @RequestParam text: String,
        @RequestParam id: Long
    ) {
        telegramBot.text(id, text)
    }

    @GetMapping("/test/save")
    fun test3() {
        dslContext.insertInto(CHAT)
            .set(CHAT.ID, 123)
            .set(CHAT.CHAT_MODE, "TEST")
            .execute()
    }
}