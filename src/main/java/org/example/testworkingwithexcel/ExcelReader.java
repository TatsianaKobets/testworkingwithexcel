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
 * To read data from a work book, data parsing and saving in JSON
 */
public class ExcelReader {

  public void readExcelFile() throws Exception {
    XSSFWorkbook workbook = new XSSFWorkbook(new File(TESTDATA_XLSX_FILE_PATH));
    ObjectMapper mapper = new ObjectMapper();
    ObjectNode jsonRoot = mapper.createObjectNode();

    workbook.forEach(currentSheet -> {
      System.out.println(
          "\nЛист " + workbook.getSheetIndex(currentSheet) + " => " + currentSheet.getSheetName());

      List<List<String>> data = new ArrayList<>();

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

      ArrayNode jsonArray = mapper.createArrayNode();
      for (List<String> row : data) {
        ObjectNode jsonObject = mapper.createObjectNode();
        for (int i = 0; i < row.size(); i++) {
          jsonObject.put("column" + (i + 1), row.get(i));
        }
        jsonArray.add(jsonObject);
        System.out.println("JSON Object: " + jsonObject);
      }

      System.out.println("JSON Array: " + jsonArray);
      jsonRoot.set(currentSheet.getSheetName(), jsonArray);
    });

    File outputFile = new File("output.json");
    if (outputFile.exists()) {
      outputFile.delete();
    }
    mapper.writeValue(outputFile, jsonRoot);
  }
}
