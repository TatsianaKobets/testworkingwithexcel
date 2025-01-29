package org.example.testworkingwithexcel;

import java.util.ArrayList;
import java.util.List;

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
}