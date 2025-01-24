package org.example.testworkingwithexcel;

import java.util.ArrayList;
import java.util.List;

/**
 * для представления производственных центров, включая их характеристики (максимальное количество сотрудников, время обработки одной детали) и методы для управления сотрудниками и деталями.
 */
public class ProductionCenter {
  private String id;
  private String name;
  private double performance;
  private double maxWorkersCount;
  private List<Employee> employees;
  private List<Part> buffer;
  private List<Connection> connections;

  public ProductionCenter(String id, String name, double performance, double maxWorkersCount) {
    this.id = id;
    this.name = name;
    this.performance = performance;
    this.maxWorkersCount = maxWorkersCount;
    this.employees = new ArrayList<>();
    this.buffer = new ArrayList<>();
    this.connections = new ArrayList<>();
  }

  public ProductionCenter(String id, String name, double performance, double maxWorkersCount, List<Employee> employees, List<Part> buffer, List<Connection> connections) {
    this.id = id;
    this.name = name;
    this.performance = performance;
    this.maxWorkersCount = maxWorkersCount;
    this.employees = employees;
    this.buffer = buffer;
    this.connections = connections;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getPerformance() {
    return performance;
  }

  public void setPerformance(double performance) {
    this.performance = performance;
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

  public void setMaxWorkersCount(double maxWorkersCount) {
    this.maxWorkersCount = maxWorkersCount;
  }

  public List<Employee> getEmployees() {
    return employees;
  }

  public void addEmployee(Employee employee) {
    employees.add(employee);
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

  public List<Connection> getConnections() {
    return connections;
  }

  public void addConnection(Connection connection) {
    connections.add(connection);
  }

  @Override
  public String toString() {
    return "ProductionCenter{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        ", performance=" + performance +
        ", maxWorkersCount=" + maxWorkersCount +
        ", employees=" + employees +
        ", buffer=" + buffer +
        ", connections=" + connections +
        '}';
  }
}