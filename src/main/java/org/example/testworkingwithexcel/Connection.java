package org.example.testworkingwithexcel;

/**
 * Representation of connections between production centers.
 */
public class Connection {
  private ProductionCenter from;
  private ProductionCenter to;

  public Connection(ProductionCenter from, ProductionCenter to) {
    this.from = from;
    this.to = to;
  }

  public ProductionCenter getFrom() {
    return from;
  }

  public void setFrom(ProductionCenter from) {
    this.from = from;
  }

  public ProductionCenter getTo() {
    return to;
  }

  public void setTo(ProductionCenter to) {
    this.to = to;
  }
  public void addPart(Part part) {
    to.addPart(part);
  }
  @Override
  public String toString() {
    return "Connection{" +
        "from=" + from.getName() +
        ", to=" + to.getName() +
        '}';
  }
}