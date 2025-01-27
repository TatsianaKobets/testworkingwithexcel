package org.example.testworkingwithexcel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * Modeling of the process of processing parts at production centers, taking into account the given
 * general number of employees.
 */
public class Simulation {

  private List<ProductionCenter> productionCenters;
  private List<Employee> employees;
  private int totalEmployees;
  private double time;

  public Simulation(List<ProductionCenter> productionCenters, List<Employee> employees,
      int totalEmployees) {
    this.productionCenters = productionCenters;
    this.employees = employees;
    this.totalEmployees = totalEmployees;
    this.time = 0;
  }

  public void runSimulation() throws IOException {
    File resultFile = new File("result.csv");
    if (resultFile.exists()) {

      System.out.println("File already exists. Deleting it for a new simulation.");
      resultFile.delete(); // Удаляем файл, если он существует
    } else {

      resultFile.createNewFile();
    }
    try (BufferedWriter writer = new BufferedWriter(
        new OutputStreamWriter(new FileOutputStream(resultFile), "UTF-8"))) {

      writer.write("Time,ProductionCenter,WorkersCount,BufferCount");

      for (ProductionCenter productionCenter : productionCenters) {
        for (int i = 0; i < 10; i++) {
          productionCenter.addPart(new Part(i, 5.0));
        }
      }

      while (time < 1000 && hasUnprocessedParts()) {
        System.out.println("Время: " + time);

        redistributeEmployees();

        for (ProductionCenter productionCenter : productionCenters) {
          processParts(productionCenter);
          System.out.println("Центр: " + productionCenter.getName() +
              ", Работники: " + productionCenter.getWorkersCount() +
              ", Буфер: " + productionCenter.getBuffer().size());

          int workersCount = (int) productionCenter.getWorkersCount();
          int bufferCount = productionCenter.getBuffer().size();

          writer.write(
              String.format("\n%.1f,%s,%d,%d", time, productionCenter.getName(), workersCount,
                  bufferCount));
        }

        time++;
      }
    }
    System.out.println("Симуляция завершена");
  }

  private boolean hasUnprocessedParts() {
    for (ProductionCenter productionCenter : productionCenters) {
      if (!productionCenter.getBuffer().isEmpty()) {
        for (Part part : productionCenter.getBuffer()) {
          if (!part.isProcessed()) {
            return true;
          }
        }
      }
    }
    return false;
  }

  private void redistributeEmployees() {
    for (ProductionCenter productionCenter : productionCenters) {
      if (productionCenter.getFreeWorkersCount() > 0) {
        for (ProductionCenter otherCenter : productionCenters) {
          if (!productionCenter.equals(otherCenter)
              && otherCenter.getFreeWorkersCount() < otherCenter.getMaxWorkersCount()) {
            System.out.println("Распределение сотрудников между центрами");
            Employee employee = productionCenter.getEmployees().stream().filter(Employee::isFree)
                .findFirst().orElse(null);
            if (employee != null) {
              employee.moveBetweenProductionCenters(productionCenter, otherCenter);
            }
          }
        }
      }
    }
  }

  private void processParts(ProductionCenter productionCenter) {
    for (Part part : productionCenter.getBuffer()) {
      if (part.isProcessed()) {
        productionCenter.removePart(part);
      } else {
        for (Employee employee : productionCenter.getEmployees()) {
          if (employee.isFree()) {
            employee.assignToProductionCenter(productionCenter);
            part.setCurrentProductionCenter(productionCenter);
            part.setProcessingTime(part.getProcessingTime() - 1);
            if (part.getProcessingTime() <= 0) {
              part.setCurrentProductionCenter(null);
              employee.freeFromProductionCenter();
            }
          }
        }
      }
    }
  }
}