package org.example.testworkingwithexcel;

import java.util.List;

/**
 * моделирования процесса обработки деталей на производственных центрах с учетом заданного общего количества сотрудников.
 */
public class Simulation {
//метод runSimulation в классе Simulation для запуска симуляции и записи результатов в CSV-файл.
private List<ProductionCenter> productionCenters;
  private List<Employee> employees;
  private int totalEmployees;

  public Simulation(List<ProductionCenter> productionCenters, List<Employee> employees, int totalEmployees) {
    this.productionCenters = productionCenters;
    this.employees = employees;
    this.totalEmployees = totalEmployees;
  }

  public void runSimulation() {
    // Запуск симуляции и запись результатов в CSV-файл
    // ...
  }
}
