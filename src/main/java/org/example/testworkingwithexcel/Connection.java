package org.example.testworkingwithexcel;

import java.util.List;

/**
 * представления связей между производственными центрами
 */
public class Connection {
  private ProductionCenter from;
  private ProductionCenter to;

  public Connection(ProductionCenter from, ProductionCenter to) {
    this.from = from;
    this.to = to;
  }

  public Connection(String sourceCenterId, String targetCenterId, List<ProductionCenter> productionCenters) {
    this.from = findProductionCenterById(sourceCenterId, productionCenters);
    this.to = findProductionCenterById(targetCenterId, productionCenters);
  }

  public ProductionCenter getFrom() {
    return from;
  }

  public ProductionCenter getTo() {
    return to;
  }

  @Override
  public String toString() {
    return "Connection{" +
        "from=" + from +
        ", to=" + to +
        '}';
  }

  public void setSourceCenter(ProductionCenter productionCenter) {
    this.from = productionCenter;
  }

  public void setTargetCenter(ProductionCenter productionCenter) {
    this.to = productionCenter;
  }

  private ProductionCenter findProductionCenterById(String id, List<ProductionCenter> productionCenters) {
    for (ProductionCenter productionCenter : productionCenters) {
      if (productionCenter.getId().equals(id)) {
        return productionCenter;
      }
    }
    return null;
  }
}