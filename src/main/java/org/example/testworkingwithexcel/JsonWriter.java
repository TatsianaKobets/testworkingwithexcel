package org.example.testworkingwithexcel;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonWriter {

  private ObjectMapper mapper = new ObjectMapper();
  private int workerCount = 0;
  private int detailsCount = 0;
  private List<ProductionCenter> productionCenters = new ArrayList<>();
  private List<Connection> connections = new ArrayList<>();


  public JsonWriter() throws IOException {
  }

  public void writeToJson() throws IOException {

    ObjectNode jsonRoot = (ObjectNode) mapper.readTree(new File("output.json"));

    ArrayNode scenarioArray = (ArrayNode) jsonRoot.get("Scenario");
    if (scenarioArray != null && scenarioArray.size() > 1) {
      JsonNode scenarioNode = scenarioArray.get(1);
      workerCount = scenarioNode.get("column1").asInt();
      detailsCount = scenarioNode.get("column2").asInt();
    }

    ArrayNode productionCenterArray = (ArrayNode) jsonRoot.get("ProductionCenter");
    for (int i = 1; i < productionCenterArray.size(); i++) {
      JsonNode centerNode = productionCenterArray.get(i);
      String id = centerNode.get("column1").asText();
      String name = centerNode.get("column2").asText();
      double performance = centerNode.get("column3").asDouble();
      double maxWorkersCount = centerNode.get("column4").asDouble();

      ProductionCenter productionCenter = new ProductionCenter(id, name, performance,
          maxWorkersCount);
      productionCenters.add(productionCenter);
    }

    ArrayNode connectionArray = (ArrayNode) jsonRoot.get("Connection");
    for (int i = 1; i < connectionArray.size(); i++) {
      JsonNode connectionNode = connectionArray.get(i);
      String sourceCenterId = connectionNode.get("column1").asText();
      String destCenterId = connectionNode.get("column2").asText();

      Connection connection = new Connection(sourceCenterId, destCenterId, productionCenters);
      connections.add(connection);
    }

    System.out.println("Количество работников: " + workerCount);
    System.out.println("Количество деталей: " + detailsCount);
    System.out.println("Производственные центры: " + productionCenters);
    System.out.println("Соединения: " + connections);
  }

  public List<ProductionCenter> getProductionCenters() {
    return productionCenters;
  }

  public int getWorkerCount() {
    return workerCount;
  }

  public int getDetailsCount() {
    return detailsCount;
  }
}