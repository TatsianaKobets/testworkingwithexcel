package org.example.testworkingwithexcel;

/**
 * To represent employees who can work at production centers.
 */
public class Employee {

  private int id;
  private EmployeeStatus status;
  private ProductionCenter currentProductionCenter;

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

  public void moveBetweenProductionCenters(ProductionCenter from, ProductionCenter to) {
    if (isFree()) {
      from.removeEmployee(this);
      to.addEmployee(this);
      assignToProductionCenter(to);
    }
  }

  public void freeFromProductionCenter() {
    this.currentProductionCenter = null;
    this.status = EmployeeStatus.FREE;
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