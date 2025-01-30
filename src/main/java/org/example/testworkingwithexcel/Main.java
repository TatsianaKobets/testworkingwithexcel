package org.example.testworkingwithexcel;

import java.util.ArrayList;
import java.util.List;

public class Main {

  public static final String TESTDATA_XLSX_FILE_PATH = "src/main/resources/testdata.xlsx";

  public static void main(String[] args) throws Exception {
    ExcelReader excelReader = new ExcelReader();
    excelReader.readExcelFile();

    JsonWriter jsonWriter = new JsonWriter();
    jsonWriter.writeToJson();

    List<ProductionCenter> productionCenters = jsonWriter.getProductionCenters();
    int workerCount = jsonWriter.getWorkerCount();
    int detailsCount = jsonWriter.getDetailsCount();

    Simulation simulation = new Simulation(productionCenters, createEmployees(workerCount),
        detailsCount, workerCount, jsonWriter.getConnections());
    simulation.runSimulation();
  }

  private static List<Employee> createEmployees(int count) {
    List<Employee> employees = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      employees.add(new Employee(i + 1));
    }
    return employees;
  }
}