package org.example.testworkingwithexcel.entity;

/**
 * A detail that will be processed at production centers.
 */
public class Part {

  private int id;
  private double processingTime;

  public Part(int id, double processingTime) {
    this.id = id;
    this.processingTime = processingTime;
  }

  public int getId() {
    return id;
  }

  public double getProcessingTime() {
    return processingTime;
  }
}