package org.example.testworkingwithexcel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * To represent production centers, including their characteristics (maximum number of employees,
 * processing time of one part) and methods for managing employees and details.
 */
public class ProductionCenter {
  private String id;
  private String name;
  private double performance;
  private double maxWorkersCount;
  private List<Employee> employees;
  private List<Connection> connections;
private List<Part> buffer = new ArrayList<>();
  public ProductionCenter(String id, String name, double performance, double maxWorkersCount) {
    this.id = id;
    this.name = name;
    this.performance = performance;
    this.maxWorkersCount = maxWorkersCount;
    this.employees = new ArrayList<>();
    this.connections = new ArrayList<>();
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public double getPerformance() {
    return performance;
  }

  public double getWorkersCount() {
    return employees.size();
  }

  public double getFreeWorkersCount() {
    return maxWorkersCount - getWorkersCount();
  }

  public double getMaxWorkersCount() {
    return maxWorkersCount;
  }

  public List<Employee> getEmployees() {
    return employees;
  }

  public void removeEmployee(Employee employee) {
    employees.remove(employee);
  }

  public List<Part> getBuffer() {
    return buffer;
  }

  public void addPart(Part part) {
    buffer.add(part);
  }

  public void removePart(Part part) {
    buffer.remove(part);
  }


  public boolean canAddEmployee() {
    return getWorkersCount() < maxWorkersCount;
  }

  public void addEmployee(Employee employee) {
    if (canAddEmployee()) {
      employees.add(employee);
    } else {
      throw new IllegalArgumentException("Превышено максимальное количество сотрудников");
    }
  }

  public void addConnection(Connection connection) {
    connections.add(connection);
  }
  @Override
  public String toString() {
    return "ProductionCenter{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        '}';
  }
  ExecutorService executor = Executors.newFixedThreadPool(1); // создаем пул потоков с 5 потоками

  public void processParts() {
    for (Employee employee : employees) {
      if (employee.isFree() && !buffer.isEmpty()) {
        Part part = buffer.remove(0);
        employee.assignToProductionCenter(this);
        System.out.println("Сотрудник " + employee.getId() + " начинает обработку детали " + part.getId());

        double processingTime = part.getProcessingTime() * (1 / performance);

        // Имитация обработки детали
        executor.submit(() -> {
          try {
            Thread.sleep((long) (processingTime * 1000)); // Имитация времени обработки в миллисекундах
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          System.out.println("Сотрудник " + employee.getId() + " завершил обработку детали " + part.getId());
          moveToNextProductionCenter(part); // Перемещение детали в следующий цех
          employee.setStatus(EmployeeStatus.FREE); // Освобождаем сотрудника
          System.out.println("Сотрудник " + employee.getId() + " свободен");
        });
      }
    }
  }

  private void moveToNextProductionCenter(Part part) {
    for (Connection connection : connections) {
      if (connection.getFrom() == this) {
        connection.addPart(part);
        System.out.println("Деталь " + part.getId() + " перемещена в следующий цех: " + connection.getTo().getName());
        break;
      }
    }
  }
}