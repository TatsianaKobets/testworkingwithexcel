package org.example.testworkingwithexcel;

/**
 * для представления сотрудников, которые могут работать на производственных центрах
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

  public Employee(int id, EmployeeStatus status, ProductionCenter currentProductionCenter) {
    this.id = id;
    this.status = status;
    this.currentProductionCenter = currentProductionCenter;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public EmployeeStatus getStatus() {
    return status;
  }

  public void setStatus(EmployeeStatus status) {
    this.status = status;
  }

  public ProductionCenter getCurrentProductionCenter() {
    return currentProductionCenter;
  }

  public void setCurrentProductionCenter(
      ProductionCenter currentProductionCenter) {
    this.currentProductionCenter = currentProductionCenter;
  } public boolean isFree() {
    return status == EmployeeStatus.FREE;
  }

  public boolean isBusy() {
    return status == EmployeeStatus.BUSY;
  }

  public void assignToProductionCenter(ProductionCenter productionCenter) {
    this.currentProductionCenter = productionCenter;
    this.status = EmployeeStatus.BUSY;
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