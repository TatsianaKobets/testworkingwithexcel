package org.example.testworkingwithexcel;

import java.io.IOException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

public class Main {

  public static final String TESTDATA_XLSX_FILE_PATH = "src/main/resources/testdata.xlsx";

  public static void main(String[] args) throws Exception {
    ExcelReader excelReader = new ExcelReader();
    excelReader.readExcelFile();

    JsonWriter jsonWriter = new JsonWriter();
    jsonWriter.writeToJson();
  }
}
  /*public static void main(String[] args) throws IOException, InvalidFormatException {
    // Создание книги из файла Excel (.xls or .xlsx). Открываем файл Excel
    Workbook workbook = WorkbookFactory.create(new File(TESTDATA_XLSX_FILE_PATH));

    // Получить количество листов в рабочей книге
    System.out.println(
        "В рабочей тетради Excel содержится " + workbook.getNumberOfSheets() + " листов : ");
    //Получить название каждого листа
    workbook.forEach(sheet -> {
      System.out.println("Лист " + workbook.getSheetIndex(sheet) + " => " + sheet.getSheetName());
    });
    System.out.println("\t");

    //Получить данные каждого листа
    workbook.forEach(sheet -> {
      System.out.println("Лист " + workbook.getSheetIndex(sheet) + " => " + sheet.getSheetName());

      // Создайте DataFormatter для форматирования и получите значение каждой ячейки в виде строки.
      DataFormatter dataFormatter = new DataFormatter();

      // Вывод значений ячейки
      sheet.forEach(row -> {
        row.forEach(cell -> {
          printCellValue(cell);
        });
        System.out.println();
      });
    });
    workbook.close();
  }

  // Вывод значений ячейки
  private static void printCellValue(Cell cell) {
    switch (cell.getCellTypeEnum()) {
      case BOOLEAN:
        System.out.print(cell.getBooleanCellValue());
        break;
      case STRING:
        System.out.print(cell.getRichStringCellValue().getString());
        break;
      case NUMERIC:
        if (DateUtil.isCellDateFormatted(cell)) {
          System.out.print(cell.getDateCellValue());
        } else {
          System.out.print(cell.getNumericCellValue());
        }
        break;
      case FORMULA:
        System.out.print(cell.getCellFormula());
        break;
      case BLANK:
        System.out.print("");
        break;
      default:
        System.out.print("");
    }

    System.out.print("\t");
  }
}*/


