package ege.bot.service.helper_service

import ege.bot.model.Result
import ege.bot.model.Student
import ege.bot.model.StudentResults
import ege.bot.service.dao.ResultDao
import ege.bot.service.dao.StudentDao
import org.apache.poi.ss.usermodel.CellType
import org.springframework.stereotype.Service

@Service
class ResultExcelService(
    private val excelService: ExcelService,
    private val studentDao: StudentDao,
    private val resultDao: ResultDao
) {

    fun createExcel(studentsMap: Map<Long, List<Student>>, results: List<Result>): ByteArray {
        val data = results.map {
            val student = studentsMap[it.studentId]!!.first()
            StudentResults(
                it.getResultText(),
                it.subject,
                student.surname ?: "паспорт: ${student.passportSeries} ${student.passportNumber}",
                student.passportSeries,
                student.passportNumber,
                student.examYear,
                student.examType
            )
        }
            .groupBy { "${it.examType.name} ${it.year}" }
            .mapValues { (_, sr) -> Pair(HEADER_SETTINGS, sr.sortedWith(compareBy(
                {res -> res.subject},
                {res -> res.name}
            ))) }

        return excelService.buildExcelFile(data) { row, result ->
            row.createCell(0, CellType.STRING).setCellValue(result.subject)
            row.createCell(1, CellType.STRING).setCellValue(result.name)
            row.createCell(2, CellType.STRING).setCellValue(result.result)
        }
    }

    fun createResultExcelFile(chatId: Long): ByteArray? {
        val studentsMap = studentDao.selectStudents(chatId).groupBy { it.id }
        val results = resultDao.getResults(chatId)

        if (studentsMap.isEmpty() || results.isEmpty()) {
            return null
        }

        return createExcel(studentsMap, results)
    }

    companion object {

        @JvmStatic
        private val HEADER_SETTINGS = listOf(
            Pair("Предмет", 6500),
            Pair("Ученик", 10000),
            Pair("Результат", 5000)
        )
    }
}