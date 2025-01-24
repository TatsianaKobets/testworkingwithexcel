package org.example.testworkingwithexcel;

import static org.example.testworkingwithexcel.Main.TESTDATA_XLSX_FILE_PATH;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * для чтения данных из рабочей книги, парсинг данных и сохранения в JSON
 */
public class ExcelReader {

  public void readExcelFile() throws Exception {
    XSSFWorkbook workbook = new XSSFWorkbook(new File(TESTDATA_XLSX_FILE_PATH));
    List<List<String>> data = new ArrayList<>();
    workbook.forEach(currentSheet -> {
      System.out.println(
          "Лист " + workbook.getSheetIndex(currentSheet) + " => " + currentSheet.getSheetName());

      // Читайте данные из Excel файла
      for (int i = 1; i <= currentSheet.getLastRowNum(); i++) {
        XSSFRow row = (XSSFRow) currentSheet.getRow(i);
        List<String> rowData = new ArrayList<>();
        for (int j = 0; j < row.getLastCellNum(); j++) {
          XSSFCell cell = row.getCell(j);
          String value = "";
          switch (cell.getCellType()) {
            case STRING:
              value = cell.getStringCellValue();
              break;
            case NUMERIC:
              value = String.valueOf(cell.getNumericCellValue());
              break;
            case BOOLEAN:
              value = String.valueOf(cell.getBooleanCellValue());
              break;
            default:
              value = "";
          }
          rowData.add(value);
        }
        data.add(rowData);
      }
    });

    // Создайте JSON объект
    ObjectMapper mapper = new ObjectMapper();
    ArrayNode jsonArray = mapper.createArrayNode();
    for (List<String> row : data) {
      ObjectNode jsonObject = mapper.createObjectNode();
      for (int i = 0; i < row.size(); i++) {
        jsonObject.put("column" + (i + 1), row.get(i));
      }
      jsonArray.add(jsonObject);
    }

    // Сохраните JSON объект в файл
    mapper.writeValue(new File("output.json"), jsonArray);
  }
}
