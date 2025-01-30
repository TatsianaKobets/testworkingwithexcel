package org.example.testworkingwithexcel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Modeling of the process of processing parts at production centers, taking into account the given
 * general number of employees.
 */
public class Simulation {

  private static final String RESULT_FILE = "result.csv";
  private List<ProductionCenter> productionCenters;
  private List<Employee> employees;
  private int detailsCount;
  private double time;
  private List<Connection> connections;

  public Simulation(List<ProductionCenter> productionCenters, List<Employee> employees,
      int detailsCount, int workerCount, List<Connection> connections) {
    this.productionCenters = productionCenters;
    this.employees = employees;
    this.detailsCount = detailsCount;
    this.time = 0;
    this.connections = connections;
  }

  public void runSimulation() throws IOException {
    File resultFile = new File(RESULT_FILE);
    if (resultFile.exists()) {
      resultFile.delete();
    }
    resultFile.createNewFile();

    distributeEmployeesToCenters();
    List<Part> parts = getParts(detailsCount);
    ProductionCenter firstCenter = productionCenters.get(0);
    parts.forEach(firstCenter::addPart);

    try (BufferedWriter writer = new BufferedWriter(
        new OutputStreamWriter(new FileOutputStream(resultFile), "UTF-8"))) {

      writer.write("Time,ProductionCenter,WorkersCount,BufferCount");

      System.out.println("\n=== НАЧАЛО СИМУЛЯЦИИ ===");
      System.out.printf("Всего деталей: %d | Сотрудников: %d\n", detailsCount, employees.size());
      printStatusHeader();

      while (!isSimulationFinished()) {
        System.out.printf("\n=== Время: %.1f мин ===\n", time);

        processStep();
        writeCSV(writer);

        time += 1.0;
      }

      System.out.println("\n=== СИМУЛЯЦИЯ ЗАВЕРШЕНА ===");
      printFinalStats();
    }
  }

  private void processStep() {
    productionCenters.forEach(pc -> pc.processParts(time));
    transferPartsBetweenCenters();
    redistributeEmployees();
    printCurrentStatus();
  }

  private void printStatusHeader() {
    System.out.println("\nЦех\t| Буфер | В работе    | Сотрудники | Статус");
    System.out.println("--------------------------------------------------");
  }

  private void printCurrentStatus() {
    productionCenters.forEach(pc -> {
      System.out.printf("%-5s\t| %-5d | %-11s | %2d/%-2d     | %s\n",
          pc.getName(),
          pc.getBuffer().size(),
          pc.getProcessingParts(),
          pc.getEmployees().size(),
          (int) pc.getMaxWorkersCount(),
          pc.getStatus());
    });
  }

  private void transferPartsBetweenCenters() {
    connections.forEach(conn -> {
      List<Part> parts = new ArrayList<>(conn.getBuffer());
      parts.forEach(part -> {
        conn.getTo().addPart(part);
        conn.removePart(part);
        System.out.printf("[%.1f] %s -> %s: Деталь %d\n",
            time, conn.getFrom().getName(), conn.getTo().getName(), part.getId());
      });
    });
  }

  private void printFinalStats() {
    System.out.println("\nИтоговая статистика:");
    productionCenters.forEach(pc ->
        System.out.printf("%s: %d деталей обработано\n", pc.getName(), pc.getProcessedCount())
    );
  }

  private void writeCSV(BufferedWriter writer) throws IOException {
    for (ProductionCenter pc : productionCenters) {
      writer.write(String.format("\n%.1f,%s,%d,%d",
          time,
          pc.getName(),
          pc.getEmployees().size(),
          pc.getBuffer().size()));
    }
  }

  private boolean isSimulationFinished() {
    return productionCenters.stream().noneMatch(pc ->
        !pc.getBuffer().isEmpty() ||
            pc.getEmployees().stream().anyMatch(Employee::isBusy) ||
            !pc.getProcessingParts().isEmpty()
    );
  }

  private void redistributeEmployees() {
    List<ProductionCenter> sortedCenters = new ArrayList<>(productionCenters);
    sortedCenters.sort((pc1, pc2) -> {
      double load1 = pc1.getBuffer().size() / (pc1.getEmployees().size() + 1);
      double load2 = pc2.getBuffer().size() / (pc2.getEmployees().size() + 1);
      return Double.compare(load2, load1);
    });

    for (int i = 0; i < sortedCenters.size(); i++) {
      ProductionCenter targetCenter = sortedCenters.get(i);
      for (int j = sortedCenters.size() - 1; j > i; j--) {
        ProductionCenter sourceCenter = sortedCenters.get(j);

        if (canMoveEmployee(sourceCenter, targetCenter)) {
          moveEmployee(sourceCenter, targetCenter);
          System.out.println(
              "Сотрудник перемещен из " + sourceCenter.getName() + " в "
                  + targetCenter.getName());
        }
      }
    }
  }

  private boolean canMoveEmployee(ProductionCenter source, ProductionCenter target) {
    boolean hasFreeEmployee = source.getEmployees().stream().anyMatch(Employee::isFree);
    boolean hasSpaceInTarget = target.getEmployees().size() < target.getMaxWorkersCount();
    return hasFreeEmployee && hasSpaceInTarget;
  }

  private void moveEmployee(ProductionCenter source, ProductionCenter target) {
    Optional<Employee> freeEmployee = source.getEmployees().stream()
        .filter(Employee::isFree)
        .findFirst();

    if (freeEmployee.isPresent()) {
      Employee employee = freeEmployee.get();
      source.removeEmployee(employee);
      target.addEmployee(employee);
      employee.assignToProductionCenter(target);
      System.out.println("Сотрудник перемещен из " + source.getName() + " в " + target.getName());
    }
  }

  public List<Part> getParts(int detailsCount) {
    if (detailsCount <= 0) {
      throw new IllegalArgumentException("Подсчет деталей должен быть больше 0");
    }

    List<Part> parts = new ArrayList<>();
    for (int i = 0; i < detailsCount; i++) {
      double processingTime = 0.2; // Время обработки детали
      parts.add(new Part(i, processingTime));
      System.out.println("Список деталей: " + parts);
    }
    return parts;

  }

  private void distributeEmployeesToCenters() {
    int remainingEmployees = employees.size();

    for (ProductionCenter productionCenter : productionCenters) {
      int maxEmployeesToAdd = (int) (productionCenter.getMaxWorkersCount()
          - productionCenter.getEmployees().size());
      int employeesToAssign = Math.min(maxEmployeesToAdd, remainingEmployees);
      for (int i = 0; i < employeesToAssign; i++) {
        Employee employee = employees.get(0);
        employee.assignToProductionCenter(productionCenter);
        productionCenter.addEmployee(employee);
        System.out.println(
            "Сотрудник " + employee.getId() + " назначен на центр " + productionCenter.getName());
        employees.remove(0);
        remainingEmployees--;
      }
    }

    System.out.println("Осталось незадействованных сотрудников: " + remainingEmployees);
  }
}
