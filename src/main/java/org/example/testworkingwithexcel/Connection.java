package org.example.testworkingwithexcel;

import java.util.List;

/**
 * Representation of connections between production centers.
 */
public class Connection {

  private ProductionCenter from;
  private ProductionCenter to;

  public Connection(String sourceCenterId, String targetCenterId,
      List<ProductionCenter> productionCenters) {
    this.from = findProductionCenterById(sourceCenterId, productionCenters);
    this.to = findProductionCenterById(targetCenterId, productionCenters);
  }

  @Override
  public String toString() {
    return "Connection{" +
        "from=" + from +
        ", to=" + to +
        '}';
  }

  private ProductionCenter findProductionCenterById(String id,
      List<ProductionCenter> productionCenters) {
    for (ProductionCenter productionCenter : productionCenters) {
      if (productionCenter.getId().equals(id)) {
        return productionCenter;
      }
    }
    return null;
  }
}