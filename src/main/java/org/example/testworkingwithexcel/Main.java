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