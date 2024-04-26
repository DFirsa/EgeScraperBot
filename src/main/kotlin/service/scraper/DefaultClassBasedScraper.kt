package ege.bot.service.scraper

import ege.bot.model.Result
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class DefaultClassBasedScraper(
    private val subjectSelector: String,
    private val resultSelector: String,
    private val examSuccessResult: String
): Scraper {
    override fun getResults(document: Document): List<Result> {
        val subjects = document.getElementsByClass(subjectSelector).map(DefaultClassBasedScraper::extractText)
        val results = document.getElementsByClass(resultSelector).map(DefaultClassBasedScraper::extractText)

        return subjects.zip(results)
            .map { (subject, result) -> buildResult(subject, result) }
    }

    private fun buildResult(subject: String, result: String): Result {
        val pointResult = result.toIntOrNull()
        val examResult = if (pointResult == null) result.lowercase() == examSuccessResult else null

        check(pointResult != null || examResult != null)
        return Result(subject, examResult, pointResult)
    }

    companion object {
        @JvmStatic
        private fun extractText(element: Element): String {
            element.select("*").remove()
            return element.text()
        }
    }
}