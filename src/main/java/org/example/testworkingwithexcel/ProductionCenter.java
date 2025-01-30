package org.example.testworkingwithexcel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * To represent production centers, including their characteristics (maximum number of employees,
 * processing time of one part) and methods for managing employees and details.
 */
public class ProductionCenter {

  private String id;
  private String name;
  private double performance;
  private double maxWorkersCount;
  private List<Employee> employees = new ArrayList<>();
  private List<Connection> connections = new ArrayList<>();
  private List<Part> buffer = new ArrayList<>();
  private Map<Part, Double> processingParts = new HashMap<>();
  private int processedCount;

  public ProductionCenter(String id, String name, double performance, double maxWorkersCount) {
    this.id = id;
    this.name = name;
    this.performance = performance;
    this.maxWorkersCount = maxWorkersCount;
  }

  public String getId() {
    return id;
  }

  public double getPerformance() {
    return performance;
  }

  public void processParts(double currentTime) {
    List<Part> toRemove = new ArrayList<>();
    processingParts.forEach((part, finishTime) -> {
      if (currentTime >= finishTime) {
        toRemove.add(part);
        processedCount++;
        System.out.printf("[%.1f] %s завершил деталь %d\n", currentTime, name, part.getId());
        connections.stream()
            .filter(c -> c.getFrom() == this)
            .findFirst()
            .ifPresent(c -> c.addPart(part));
      }
    });
    toRemove.forEach(processingParts::remove);

    employees.stream()
        .filter(Employee::isFree)
        .forEach(emp -> {
          if (!buffer.isEmpty()) {
            Part part = buffer.remove(0);
            double processTime = part.getProcessingTime() / performance;
            processingParts.put(part, currentTime + processTime);
            emp.setStatus(EmployeeStatus.BUSY);
            System.out.printf("[%.1f] %s начал деталь %d (%.1f мин)\n",
                currentTime, name, part.getId(), processTime);
          }
        });
  }

  public String getProcessingParts() {
    return processingParts.keySet().stream()
        .map(p -> String.valueOf(p.getId()))
        .collect(Collectors.joining(", "));
  }

  public String getStatus() {
    long busy = employees.stream().filter(Employee::isBusy).count();
    return String.format("%d/%d занято", busy, employees.size());
  }

  public int getProcessedCount() {
    return processedCount;
  }

  public List<Part> getBuffer() {
    return buffer;
  }

  public void addPart(Part part) {
    buffer.add(part);
  }

  public List<Employee> getEmployees() {
    return employees;
  }

  public void addEmployee(Employee e) {
    employees.add(e);
  }

  public void removeEmployee(Employee e) {
    employees.remove(e);
  }

  public double getMaxWorkersCount() {
    return maxWorkersCount;
  }

  public String getName() {
    return name;
  }
}