package org.example.testworkingwithexcel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Modeling of the process of processing parts at production centers, taking into account the given
 * general number of employees.
 */
public class Simulation {
  private List<ProductionCenter> productionCenters;
  private List<Employee> employees;
  private int detailsCount;
  private double time;
  private List<Connection> connections;
  private JsonWriter jsonWriter = new JsonWriter();

  public Simulation(List<ProductionCenter> productionCenters, List<Employee> employees,
      int detailsCount,
      int count, List<Connection> connections) throws IOException {
    this.productionCenters = productionCenters;
    this.employees = employees;
    this.detailsCount = detailsCount;
    this.time = 0;
    this.connections = connections;
  }


  public void runSimulation() throws IOException {
    File resultFile = new File("result.csv");
    if (resultFile.exists()) {
      System.out.println("\n Файл уже существует. Удаляем его для новой симуляции.");
      resultFile.delete();
    } else {
      resultFile.createNewFile();
    }

    // Подготовка к симуляции
    System.out.println("Подготовка к симуляции");

    System.out.println("Распределяем сотрудников в цехах");
    System.out.println("Всего сотрудников: " + employees.size());
    // Распределяем сотрудников по цехам
    distributeEmployeesToCenters();
    // Добавить все детали в буфер первого ПЦ
    ProductionCenter firstCenter = productionCenters.get(1);

    List<Part> startparts = getParts(detailsCount);
    System.out.println("Список деталей: " + startparts);
    System.out.println("Количество деталей: " + startparts.size());

    System.out.println("Добавляем детали в буфер первого цеха " + firstCenter.getName());
    if(productionCenters == firstCenter){
      for (Part part : startparts) {
        firstCenter.addPart(part);
      }
      System.out.println("Буфер первого цеха: " + firstCenter.getBuffer().size());
    }else{

    }
    try (BufferedWriter writer = new BufferedWriter(
        new OutputStreamWriter(new FileOutputStream(resultFile), "UTF-8"))) {
      writer.write("Time,ProductionCenter,WorkersCount,BufferCount");

      for (ProductionCenter productionCenter : productionCenters) {
        int workersCount = (int) productionCenter.getWorkersCount();
        int bufferCount = productionCenter.getBuffer().size();

        writer.write(
            String.format("\n%.1f,%s,%d,%d", time, productionCenter.getName(), workersCount,
                bufferCount));
      }

      System.out.println("Симуляция завершена");
    }
  }

  public List<Part> getParts(int detailsCount) {
    if (detailsCount <= 0) {
      throw new IllegalArgumentException("Details count must be greater than 0");
    }

    List<Part> parts = new ArrayList<>();
    for (int i = 0; i < detailsCount; i++) {
      // Генерировать случайное время обработки для каждой части
      double processingTime = Math.random() * 10 + 1;
      parts.add(new Part(i, processingTime));
    }
    return parts;
  }
  private void distributeEmployeesToCenters() {
    int remainingEmployees = employees.size();

    for (ProductionCenter productionCenter : productionCenters) {
      // Получаем максимальное количество сотрудников, которых можно добавить в данный цех
      int maxEmployeesToAdd = (int) (productionCenter.getMaxWorkersCount() - productionCenter.getEmployees().size());
      // Определяем, сколько сотрудников мы можем назначить
      int employeesToAssign = Math.min(maxEmployeesToAdd, remainingEmployees);
 for (int i = 0; i < employeesToAssign; i++) {
        Employee employee = employees.get(0);
        employee.assignToProductionCenter(productionCenter);
        productionCenter.addEmployee(employee);
        System.out.println("Сотрудник " + employee.getId() + " назначен на центр " + productionCenter.getName());
        employees.remove(0);
        remainingEmployees--;
      }
    }

    System.out.println("Осталось незадействованных сотрудников: " + remainingEmployees);
  }
}
