package org.example.testworkingwithexcel;

/**
 * A detail that will be processed at production centers.
 */
public class Part {

  private int id;
  private double processingTime;
  private ProductionCenter currentProductionCenter;

  public Part(int id, double processingTime) {
    this.id = id;
    this.processingTime = processingTime;
    this.currentProductionCenter = null;
  }

  public double getProcessingTime() {
    return processingTime;
  }

  public void setCurrentProductionCenter(ProductionCenter currentProductionCenter) {
    this.currentProductionCenter = currentProductionCenter;
  }

  public boolean isProcessed() {
    return currentProductionCenter != null;
  }

  public void setProcessingTime(double v) {
  }
}
