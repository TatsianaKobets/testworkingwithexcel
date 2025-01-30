package org.example.testworkingwithexcel.parsing;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.example.testworkingwithexcel.entity.ProductionCenter;
import org.example.testworkingwithexcel.logic.Connection;

public class JsonWriter {

  private static final String OUTPUT_FILE = "output.json";
  private ObjectMapper mapper = new ObjectMapper();
  private int workerCount = 0;
  private int detailsCount = 0;
  private List<ProductionCenter> productionCenters = new ArrayList<>();
  private List<Connection> connections = new ArrayList<>();

  public void writeToJson() throws IOException {
    ObjectNode jsonRoot = (ObjectNode) mapper.readTree(new File(OUTPUT_FILE));
    ArrayNode scenarioArray = (ArrayNode) jsonRoot.get("Scenario");
    if (scenarioArray != null && scenarioArray.size() > 1) {
      JsonNode scenarioNode = scenarioArray.get(1);
      workerCount = scenarioNode.get("column1").asInt();
      detailsCount = scenarioNode.get("column2").asInt();
    } else {
      throw new IOException("Массив сценариев отсутствует или пуст в файле JSON.");
    }

    ArrayNode productionCenterArray = (ArrayNode) jsonRoot.get("ProductionCenter");
    if (productionCenterArray != null && productionCenterArray.size() > 1) {
      for (int i = 1; i < productionCenterArray.size(); i++) {
        JsonNode productionCenterNode = productionCenterArray.get(i);
        String id = productionCenterNode.get("column1").asText();
        String name = productionCenterNode.get("column2").asText();
        double performance = productionCenterNode.get("column3").asDouble();
        double maxWorkersCount = productionCenterNode.get("column4").asDouble();

        ProductionCenter productionCenter = new ProductionCenter(id, name, performance,
            maxWorkersCount);
        productionCenters.add(productionCenter);
      }
    } else {
      throw new IOException("Массив производственных центров отсутствует или пуст в файле JSON.");
    }

    ArrayNode connectionArray = (ArrayNode) jsonRoot.get("Connection");
    if (connectionArray != null && connectionArray.size() > 1) {
      for (int i = 1; i < connectionArray.size(); i++) {
        JsonNode connectionNode = connectionArray.get(i);
        String sourceCenterId = connectionNode.get("column1").asText();
        String destCenterId = connectionNode.get("column2").asText();

        ProductionCenter from = productionCenters.stream()
            .filter(center -> center.getId().equals(sourceCenterId))
            .findFirst().orElse(null);
        ProductionCenter to = productionCenters.stream()
            .filter(center -> center.getId().equals(destCenterId))
            .findFirst().orElse(null);

        if (from != null && to != null) {
          Connection connection = new Connection(from, to);
          connections.add(connection);
        } else {
          if (from == null) {
            throw new IOException("Не найден источник: " + sourceCenterId);
          }
          if (to == null) {
            throw new IOException("Не найден пункт назначения: " + destCenterId);
          }
        }
      }
    } else {
      throw new IOException("Массив соединений отсутствует или пуст в файле JSON.");
    }

    jsonRoot.put("Scenario", scenarioArray);
    mapper.writeValue(new File(OUTPUT_FILE), jsonRoot);

    ArrayNode productionCenterArrayNode = mapper.createArrayNode();
    for (ProductionCenter productionCenter : productionCenters) {
      ObjectNode productionCenterNode = mapper.createObjectNode();
      productionCenterNode.put("column1", productionCenter.getId());
      productionCenterNode.put("column2", productionCenter.getName());
      productionCenterNode.put("column3", productionCenter.getPerformance());
      productionCenterNode.put("column4", productionCenter.getMaxWorkersCount());
      productionCenterArrayNode.add(productionCenterNode);
    }
    jsonRoot.put("ProductionCenter", productionCenterArray);
    mapper.writeValue(new File(OUTPUT_FILE), jsonRoot);

    ArrayNode connectionArrayNode = mapper.createArrayNode();
    for (Connection connection : connections) {
      ObjectNode connectionNode = mapper.createObjectNode();
      connectionNode.put("column1", connection.getFrom().getId());
      connectionNode.put("column2", connection.getTo().getId());
      connectionArrayNode.add(connectionNode);
    }
    jsonRoot.set("Connection", connectionArrayNode);
    mapper.writeValue(new File(OUTPUT_FILE), jsonRoot);

  }

  public int getWorkerCount() {
    return workerCount;
  }

  public int getDetailsCount() {
    return detailsCount;
  }

  public List<ProductionCenter> getProductionCenters() {
    return productionCenters;
  }

  public List<Connection> getConnections() {
    return connections;
  }
}