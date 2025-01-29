package org.example.testworkingwithexcel;

/**
 * A detail that will be processed at production centers.
 */
public class Part {
  private int id;
  private double processingTime;
  private ProductionCenter currentProductionCenter;
  private boolean isFree;

  public Part(int id, double processingTime) {
    this.id = id;
    this.processingTime = processingTime;
    //this.currentProductionCenter = null;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setFree(boolean free) {
    isFree = free;
  }

  public double getProcessingTime() {
    return processingTime;
  }

  public void setProcessingTime(double processingTime) {
    this.processingTime = processingTime;
  }

  public void setCurrentProductionCenter(ProductionCenter currentProductionCenter) {
    this.currentProductionCenter = currentProductionCenter;
  }

  public ProductionCenter getCurrentProductionCenter() {
    return currentProductionCenter;
  }

  public void moveBetweenProductionCenters(ProductionCenter from, ProductionCenter to) {
    // Логика перемещения сотрудника между производственными центрами
    isFree = true;
    // ...
  }

  public boolean isFree() {
    return isFree;
  }

  @Override
  public String toString() {
    return "Part{" +
        "id=" + id;
  }
}