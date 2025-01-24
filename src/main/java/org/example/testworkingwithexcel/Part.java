package org.example.testworkingwithexcel;

/**
 *  деталь, которая будет обрабатываться на производственных центрах.
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

  public int getId() {
    return id;
  }

  //возвращает время обработки детали на производственном центре

  public double getProcessingTime() {
    return processingTime;
  }

  //возвращает производственный центр, на котором деталь находится в настоящее время
  public ProductionCenter getCurrentProductionCenter() {
    return currentProductionCenter;
  }
//устанавливает производственный центр, на котором деталь находится в настоящее время
  public void setCurrentProductionCenter(ProductionCenter currentProductionCenter) {
    this.currentProductionCenter = currentProductionCenter;
  }
//возвращает true, если деталь уже обработана на производственном центре, и false в противном случае.
  public boolean isProcessed() {
    return currentProductionCenter != null;
  }

  public void setProcessingTime(double v) {
  }
}
