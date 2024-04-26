package ege.bot.service.scraper

import ege.bot.model.Result
import org.jsoup.nodes.Document

interface Scraper {

    fun getResults(document: Document): List<Result>
}