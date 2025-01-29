package org.example.testworkingwithexcel;

/**
 * To represent employees who can work at production centers.
 */
public class Employee {

  private int id;
  private EmployeeStatus status;
  private ProductionCenter currentProductionCenter;
  private double performance; // New attribute

  public Employee(int id, double performance) { // Constructor updated
    this.id = id;
    this.performance = performance;
    this.status = EmployeeStatus.FREE;
    this.currentProductionCenter = null;
  }

  public double getPerformance() {
    return performance;
  }

  public int getId() {
    return id;
  }

 public Employee(int id) {
    this.id = id;
    this.status = EmployeeStatus.FREE;
    this.currentProductionCenter = null;
  }

  public boolean isFree() {
    return status == EmployeeStatus.FREE;
  }

  public boolean isBusy() {
    return status == EmployeeStatus.BUSY;
  }

  public void assignToProductionCenter(ProductionCenter productionCenter) {
    this.currentProductionCenter = productionCenter;
    this.status = EmployeeStatus.BUSY;
  }

  @Override
  public String toString() {
    return "Employee{" +
        "id=" + id +
        ", status=" + status +
        ", currentProductionCenter=" + currentProductionCenter +
        '}';
  }
}