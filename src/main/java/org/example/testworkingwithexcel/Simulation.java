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
    File resultFile = new File("result.csv");
    if (resultFile.exists()) {
      System.out.println("\n Файл уже существует. Удаляем его для новой симуляции.");
      resultFile.delete();
    } else {
      resultFile.createNewFile();
    }

    // Подготовка к симуляции
    System.out.println("Подготовка к симуляции");

    // Распределяем сотрудников по цехам
    distributeEmployeesToCenters();

    // Создаем детали
    List<Part> parts = getParts(detailsCount);
    System.out.println("Список деталей: " + parts);
    System.out.println("Количество деталей: " + parts.size());

    // Добавляем все детали в буфер первого ПЦ
    ProductionCenter firstCenter = productionCenters.get(0);
    System.out.println("Добавляем детали в буфер первого цеха " + firstCenter.getName());
    for (Part part : parts) {
      firstCenter.addPart(part);
    }
    System.out.println("Буфер первого цеха: " + firstCenter.getBuffer().size());

    // Запуск симуляции
    try (BufferedWriter writer = new BufferedWriter(
        new OutputStreamWriter(new FileOutputStream(resultFile), "UTF-8"))) {
      writer.write("Time,ProductionCenter,WorkersCount,BufferCount");

      while (!isSimulationFinished()) {
        // Обработка деталей на каждом ПЦ
        for (ProductionCenter productionCenter : productionCenters) {
          productionCenter.processParts();
        }

        // Перераспределение сотрудников
        redistributeEmployees();

        // Запись текущего состояния в CSV
        for (ProductionCenter productionCenter : productionCenters) {
          int workersCount = (int) productionCenter.getWorkersCount();
          int bufferCount = productionCenter.getBuffer().size();

          writer.write(
              String.format("\n%.1f,%s,%d,%d", time, productionCenter.getName(), workersCount,
                  bufferCount));
        }

        // Увеличиваем время на 1 минуту
        time += 1.0;

        // Визуализация текущего состояния
        visualizeState();
      }
    }

    System.out.println("Симуляция завершена");
  }

  private void visualizeState() {
    System.out.println("\nТекущее состояние:");
    for (ProductionCenter center : productionCenters) {
      System.out.println(center.getName() + " - Рабочие: " + center.getWorkersCount() +
          ", Буфер: " + center.getBuffer().size());
      for (Part part : center.getBuffer()) {
        System.out.println("    Деталь " + part.getId() + " в буфере");
      }
      for (Employee employee : center.getEmployees()) {
        String status = employee.isFree() ? "свободен" : "занят";
        System.out.println("    Сотрудник " + employee.getId() + " - " + status);
      }
    }
  }

  private boolean isSimulationFinished() {
    // Проверяем, остались ли детали в буферах или в процессе обработки
    for (ProductionCenter productionCenter : productionCenters) {
      if (!productionCenter.getBuffer().isEmpty() || productionCenter.getEmployees().stream()
          .anyMatch(Employee::isBusy)) {
        System.out.println("Продолжаем симуляцию");
        return false;
      }
    }
    System.out.println("Симуляция завершена. Все детали обработаны.");
    return true;
  }

  private void redistributeEmployees() {
    // Сортируем цеха по уровню загруженности (по убыванию)
    List<ProductionCenter> sortedCenters = new ArrayList<>(productionCenters);
    sortedCenters.sort((pc1, pc2) -> {
      double load1 =
          pc1.getBuffer().size() / (pc1.getWorkersCount()
              + 1); // +1 чтобы избежать деления на ноль
      double load2 = pc2.getBuffer().size() / (pc2.getWorkersCount() + 1);
      return Double.compare(load2, load1); // Сортировка по убыванию загруженности
    });

    // Перемещаем сотрудников из менее загруженных цехов в более загруженные
    for (int i = 0; i < sortedCenters.size(); i++) {
      ProductionCenter targetCenter = sortedCenters.get(i); // Более загруженный цех
      for (int j = sortedCenters.size() - 1; j > i; j--) {
        ProductionCenter sourceCenter = sortedCenters.get(j); // Менее загруженный цех

        // Проверяем, можно ли переместить сотрудника из sourceCenter в targetCenter
        if (canMoveEmployee(sourceCenter, targetCenter)) {
          moveEmployee(sourceCenter, targetCenter);
          System.out.println(
              "Сотрудник перемещен из " + sourceCenter.getName() + " в "
                  + targetCenter.getName());
        }
      }
    }
  }

  /**
   * Проверяет, можно ли переместить сотрудника из одного цеха в другой.
   */
  private boolean canMoveEmployee(ProductionCenter source, ProductionCenter target) {
    // Проверяем, есть ли свободные сотрудники в source
    boolean hasFreeEmployee = source.getEmployees().stream().anyMatch(Employee::isFree);

    // Проверяем, есть ли место в target для нового сотрудника
    boolean hasSpaceInTarget = target.getWorkersCount() < target.getMaxWorkersCount();

    return hasFreeEmployee && hasSpaceInTarget;
  }

  /**
   * Перемещает одного свободного сотрудника из source в target.
   */
  private void moveEmployee(ProductionCenter source, ProductionCenter target) {
    // Находим первого свободного сотрудника в source
    Optional<Employee> freeEmployee = source.getEmployees().stream()
        .filter(Employee::isFree)
        .findFirst();

    if (freeEmployee.isPresent()) {
      Employee employee = freeEmployee.get();

      // Удаляем сотрудника из source
      source.removeEmployee(employee);

      // Добавляем сотрудника в target
      target.addEmployee(employee);

      // Обновляем текущий цех сотрудника
      employee.setCurrentProductionCenter(target);

      System.out.println("Сотрудник перемещен из " + source.getName() + " в " + target.getName());
    }
  }

  // Метод для получения списка деталей
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
