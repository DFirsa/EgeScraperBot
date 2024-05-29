package ege.bot.service.helper_service

import ege.bot.model.ExamType
import ege.bot.model.Student
import ege.bot.service.dao.StudentDao
import ege.bot.service.helper_service.utils.validateExam
import ege.bot.service.helper_service.utils.validateNumber
import ege.bot.service.helper_service.utils.validateSeries
import ege.bot.service.helper_service.utils.validateYear
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.springframework.stereotype.Service
import java.io.InputStream

@Service
class StudentExcelService(
    private val excelService: ExcelService,
    private val studentDao: StudentDao
) {

    fun parseExcelToLines(inputStream: InputStream): List<String> {
        val workbook = WorkbookFactory.create(inputStream)
        val sheet = workbook.getSheetAt(0)

        return sheet.rowIterator().asSequence()
            .drop(1)
            .map { (0..4).joinToString(" ") { idx ->
                getStringValue(it.getCell(idx))
            } }
            .toList()
    }

    fun createExample(): ByteArray {
        val pair = Pair(HEADER, EXAMPLE_STUDENTS)
        return excelService.buildExcelFile(mapOf(SHEET_NAME to pair)) { row, st ->
            row.createCell(0, CellType.STRING).setCellValue(st.examType.name)
            row.createCell(1, CellType.NUMERIC).setCellValue(st.examYear.toString())
            row.createCell(2, CellType.NUMERIC).setCellValue(st.passportSeries)
            row.createCell(3, CellType.STRING).setCellValue(st.passportNumber)
            row.createCell(4, CellType.STRING).setCellValue(st.surname)
        }
    }

    fun buildStudentsFile(chatId: Long): ByteArray {
        val data = studentDao.selectStudents(chatId)
            .sortedWith(compareBy(
                {it.examYear},
                {it.examType.name},
                {it.surname ?: "${it.passportSeries} ${it.passportNumber}"}
            ))
        val pair = Pair(STUDENTS_HEADER, data)
        return excelService.buildExcelFile(mapOf(SHEET_NAME to pair)) { row, st ->
            row.createCell(0, CellType.NUMERIC).setCellValue(st.id.toString())
            row.createCell(1, CellType.NUMERIC).setCellValue(st.examYear.toString())
            row.createCell(2, CellType.STRING).setCellValue(st.examType.name)
            row.createCell(3, CellType.STRING).setCellValue(st.passportSeries)
            row.createCell(4, CellType.STRING).setCellValue(st.passportNumber)
            row.createCell(5, CellType.STRING).setCellValue(st.surname)
        }
    }

    fun parseEditedStudents(inputStream: InputStream, chatId: Long): List<Student> {
        val workbook = WorkbookFactory.create(inputStream)
        val sheet = workbook.getSheetAt(0)

        return sheet.rowIterator().asSequence()
            .drop(1)
            .map {
                val passportSeries = getStringValue(it.getCell(3))
                val passportNumber = getStringValue(it.getCell(4))
                val examType = getStringValue(it.getCell(2))
                val year = getStringValue(it.getCell(1))
                Student(
                    runCatching { getStringValue(it.getCell(0)).toLong() }.getOrElse { return@map null },
                    validateNumber(passportNumber)?.let { return@map null } ?: passportNumber,
                    validateSeries(passportSeries)?.let { return@map null } ?: passportSeries,
                    chatId,
                    validateExam(examType)?.let { return@map null } ?: ExamType.valueOf(examType),
                    getStringValue(it.getCell(5)),
                    validateYear(year)?.let { return@map null } ?: year.toShort()

                )
            }
            .filterNotNull()
            .toList()
    }
    private fun getStringValue(cell: Cell?): String {
        return cell?.let {
                    when (it.cellType) {
                        CellType.NUMERIC -> it.numericCellValue.toLong().toString()
                        else -> it.stringCellValue
                    }
        } ?: ""
    }


    companion object {

        @JvmStatic
        private val EXAMPLE_STUDENTS = listOf(
            Student(
                passportSeries = "1111",
                passportNumber = "123123",
                chatId = 0,
                examType = ExamType.EGE,
                examYear = 2020,
                surname = "Петров"
            ),
            Student(
                passportSeries = "2222",
                passportNumber = "444555",
                chatId = 0,
                examType = ExamType.OGE,
                examYear = 2020,
                surname = "Иванов"
            )
        )

        @JvmStatic
        private val HEADER = listOf(
            Pair("Экзамен", 3000),
            Pair("Год", 4500),
            Pair("Серия паспорта", 4500),
            Pair("Номер паспорта", 4500),
            Pair("Имя", 10000)
        )

        private val STUDENTS_HEADER = listOf(
            Pair("id", 0),
            Pair("Год", 4500),
            Pair("Экзамен", 4500),
            Pair("Серия паспорта", 4500),
            Pair("Номер паспорта", 4500),
            Pair("ФИО", 10000)
        )

        private const val SHEET_NAME = "Данные учеников"
    }
}