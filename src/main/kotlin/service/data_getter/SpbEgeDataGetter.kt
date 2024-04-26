package ege.bot.service.data_getter

import ege.bot.model.ExamType
import ege.bot.model.IdentificationInfo
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Year

@Service
class SpbEgeDataGetter(
    @Value("\${results-source.spb-ege.url}")
    private val baseUrl: String
): DataGetter {

    override fun getPageData(examType: ExamType, year: Year, idInfo: IdentificationInfo): Document {
        val urlParams = mapOf("mode" to "${examType.id}${year}")
        val url = buildUrl(baseUrl, "", urlParams)

        return Jsoup.connect(url)
            .data("Series", idInfo.series, "Number", idInfo.number, "Login", DEFAULT_LOGIN)
            .post()
    }

    companion object {
        const val DEFAULT_LOGIN = ""
    }
}