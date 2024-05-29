package ege.bot.executor

import ege.bot.model.IdentificationInfo
import ege.bot.service.dao.ResultDao
import ege.bot.service.dao.StudentDao
import ege.bot.service.data_getter.DataGetter
import ege.bot.service.notification.NotificationService
import ege.bot.service.scraper.Scraper
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.Year
import java.time.YearMonth

@Service
class ResultUpdatingExecutor(
    private val dataGetter: DataGetter,
    private val scraper: Scraper,
    private val studentDao: StudentDao,
    private val resultDao: ResultDao,
    private val notificationService: NotificationService
) {
    @Scheduled(cron = "0 0/20 * * * *")
    fun doJob() {
        val currentYear = resolveExamYear()
        importResults(currentYear)
        notificationService.notifyUsers()
    }

    /**
     * Resolve exam year by current year.
     * If fall is now then exam in the next year, otherwise in the current year.
     */
    private fun resolveExamYear(): Year {
        val yearMonth = YearMonth.now()
        val year = Year.of(yearMonth.year)
        return if (yearMonth.month.value >= 9) year.plusYears(1) else year
    }

    /**
     * Import results every user's result using provided year
     */
    private fun importResults(year: Year) {
        val results = studentDao.selectStudentsByYear(year)
            .stream()
            .flatMap {
                val document = dataGetter.getPageData(
                    it.examType,
                    year,
                    IdentificationInfo(it.passportSeries, it.passportNumber, it.surname)
                )
                scraper.getResults(document).stream().peek { result -> result.studentId = it.id }
            }
            .toList()

        resultDao.insert(results)
    }
}