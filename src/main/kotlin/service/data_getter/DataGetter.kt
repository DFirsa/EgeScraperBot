package ege.bot.service.data_getter


import ege.bot.model.ExamType
import ege.bot.model.IdentificationInfo
import org.jsoup.nodes.Document
import java.net.URLEncoder
import java.time.Year

interface DataGetter {
    fun getPageData(examType: ExamType, year: Year, idInfo: IdentificationInfo): Document

    fun buildUrl(baseUrl: String, urlSuffix: String, queryParams: Map<String, String>): String {
        val urlQuery = queryParams
            .map { entry -> "${entry.key}=${entry.value}" }
            .joinToString("&")
        return "${baseUrl}${urlSuffix}?${urlQuery}"
    }
}