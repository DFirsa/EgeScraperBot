package ege.bot.service.data_getter

import ege.bot.model.ExamType
import ege.bot.model.IdentificationInfo
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.net.URLEncoder
import java.time.Year

@Service
class SpbEgeDataGetter(
    @Value("\${results-source.spb-ege.url}")
    private val baseUrl: String
): DataGetter {

    override fun getPageData(examType: ExamType, year: Year, idInfo: IdentificationInfo): Document {
        val urlParams = mapOf(
            "mode" to "${examType.id}${year}",
            "wave" to "2"
        )
        val url = buildUrl(baseUrl, "", urlParams)
        val data = buildData(idInfo)

        return Jsoup.connect(url)
            .header("Content-Type", "application/x-www-form-urlencoded")
            .requestBody(data)
            .post()
    }

    private fun buildData(idInfo: IdentificationInfo): String {
        val surname = URLEncoder.encode(idInfo.surname, ENCODING)
        val login = URLEncoder.encode(DEFAULT_LOGIN, ENCODING)
        return "pLastName=$surname&Series=${idInfo.series}&Number=${idInfo.number}&Login=$login"
    }

    companion object {
        const val DEFAULT_LOGIN = "Посмотреть результаты"
        const val ENCODING = "windows-1251"
    }
}