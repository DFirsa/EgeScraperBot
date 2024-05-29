package ege.bot.service.helper_service

import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream

@Service
class ExcelService {

    /**
     * @param sheetNameToHeaderAndData sheetName -> list of (Pair (header to column size) and data)
     */
    fun <T> buildExcelFile(
        sheetNameToHeaderAndData: Map<String, Pair<List<Pair<String, Int>>, List<T>>>,
        convertToRow: (XSSFRow, T) -> Unit
    ): ByteArray {
        val workbook = XSSFWorkbook()
        sheetNameToHeaderAndData.forEach { (sheetName, pair) ->
            createSheet(pair.first, pair.second, sheetName, workbook, convertToRow)
        }

        val out = ByteArrayOutputStream()
        workbook.write(out)
        return out.toByteArray()
    }

    private fun <T> createSheet(
        headerWithSize: List<Pair<String, Int>>,
        data: List<T>,
        sheetName: String,
        workbook: XSSFWorkbook,
        mapToRowFunction: (XSSFRow, T) -> Unit
    ) {
        val sheet = workbook.createSheet(sheetName)
        buildHeader(headerWithSize, sheet)
        for((idx, value) in data.withIndex()) {
            fillRow(value, idx + 1, sheet, mapToRowFunction)
        }
    }

    private fun buildHeader(dataWithSize: List<Pair<String, Int>>, sheet: XSSFSheet) {
        val row = sheet.createRow(0)
        for ((idx, value) in dataWithSize.withIndex()) {
            sheet.setColumnWidth(idx, value.second)
            val cell = row.createCell(idx)
            cell.setCellValue(value.first)
        }
    }

    private fun <T> fillRow(data: T, idx: Int, sheet: XSSFSheet, fillRowFunction: (XSSFRow, T) -> Unit) {
        val row = sheet.createRow(idx)
        fillRowFunction.invoke(row, data)
    }
}