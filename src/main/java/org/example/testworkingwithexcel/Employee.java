package org.example.testworkingwithexcel;

/**
 * To represent employees who can work at production centers.
 */
public class Employee {

  private int id;
  private EmployeeStatus status = EmployeeStatus.FREE;
  private ProductionCenter currentCenter;

  public Employee(int id) {
    this.id = id;
  }

  public boolean isBusy() {
    return status == EmployeeStatus.BUSY;
  }

  public boolean isFree() {
    return !isBusy();
  }

  public int getId() {
    return id;
  }

  public void setStatus(EmployeeStatus s) {
    status = s;
  }

  public void assignToProductionCenter(ProductionCenter pc) {
    currentCenter = pc;
  }
}