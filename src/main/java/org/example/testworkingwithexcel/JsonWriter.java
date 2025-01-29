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

  private static final String OUTPUT_FILE = "output.json";

  private ObjectMapper mapper = new ObjectMapper();
  private int workerCount = 0;
  private int detailsCount = 0;
  private List<ProductionCenter> productionCenters = new ArrayList<>();
  private List<Connection> connections = new ArrayList<>();

  public void writeToJson() throws IOException {
    // Загрузка JSON-файла
    ObjectNode jsonRoot = (ObjectNode) mapper.readTree(new File(OUTPUT_FILE));

    System.out.println("\nРабота с output.json:");
// Получить массив «сценария» и проверьте, есть ли в нем элементы
    ArrayNode scenarioArray = (ArrayNode) jsonRoot.get("Scenario");
    if (scenarioArray != null && scenarioArray.size() > 1) {
      JsonNode scenarioNode = scenarioArray.get(1); // Получаем второй элемент
      workerCount = scenarioNode.get("column1").asInt();
      detailsCount = scenarioNode.get("column2").asInt();
      System.out.println(
          "Для работы с буфером: " + "workerCount = " + workerCount + ", detailsCount = "
              + detailsCount);
    } else {
      throw new IOException("Массив сценариев отсутствует или пуст в файле JSON.");
    }

    // Получить массив «Производственный центр» и проанализировать его
    ArrayNode productionCenterArray = (ArrayNode) jsonRoot.get("ProductionCenter");
    System.out.println(
        "Производственный центр: id, name, производительность, максимальное количество рабочих");
    if (productionCenterArray != null && productionCenterArray.size() > 1) {
      for (int i = 0; i < productionCenterArray.size(); i++) {
        JsonNode productionCenterNode = productionCenterArray.get(i);
        String id = productionCenterNode.get("column1").asText();
        String name = productionCenterNode.get("column2").asText();
        double performance = productionCenterNode.get("column3").asDouble();
        double maxWorkersCount = productionCenterNode.get("column4").asDouble();

        ProductionCenter productionCenter = new ProductionCenter(id, name, performance,
             maxWorkersCount);
        productionCenters.add(productionCenter);

        System.out.println(productionCenterNode.toString());
      }
    } else {
      throw new IOException("Массив производственных центров отсутствует или пуст в файле JSON.");
    }
    // Получить массив "Соединения" (связи) и проанализировать его
    ArrayNode connectionArray = (ArrayNode) jsonRoot.get("Connection");
    if (connectionArray != null && connectionArray.size() > 1) {
      for (int i = 1; i < connectionArray.size(); i++) { // начинаем с 1, чтобы пропустить заголовок
        JsonNode connectionNode = connectionArray.get(i);
        String sourceCenterId = connectionNode.get("column1").asText(); // Получаем значение из column1
        String destCenterId = connectionNode.get("column2").asText(); // Получаем значение из column2

        ProductionCenter from = productionCenters.stream()
            .filter(center -> center.getId().equals(sourceCenterId))
            .findFirst().orElse(null);
        ProductionCenter to = productionCenters.stream()
            .filter(center -> center.getId().equals(destCenterId))
            .findFirst().orElse(null);

        if (from != null && to != null) {
          Connection connection = new Connection(from, to);
          connections.add(connection);
          System.out.println("Connection: " +  " с " + from.getId() + " в " + to.getId());
        } else {
          if (from == null) {
            System.out.println("Не найден источник: " + sourceCenterId);
          }
          if (to == null) {
            System.out.println("Не найден пункт назначения: " + destCenterId);
          }
        }
      }
    } else {
      throw new IOException("Массив соединений отсутствует или пуст в файле JSON.");
    }

    // Записать массив «сценария» в output.json
    jsonRoot.put("Scenario", scenarioArray);
    mapper.writeValue(new File("output.json"), jsonRoot);

    //Записать массив «Производственный центр» в output.json
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
    mapper.writeValue(new File("output.json"), jsonRoot);

    //Записать массив «Соединения» в output.json
    ArrayNode connectionArrayNode = mapper.createArrayNode();
    for (Connection connection : connections) {
      ObjectNode connectionNode = mapper.createObjectNode();
      connectionNode.put("column1", connection.getFrom().getId());
      connectionNode.put("column2", connection.getTo().getId());
      connectionArrayNode.add(connectionNode);
    }
    jsonRoot.set("Connection", connectionArrayNode);
    mapper.writeValue(new File("output.json"), jsonRoot);

  }

  public ObjectMapper getMapper() {
    return mapper;
  }

  public void setMapper(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  public int getWorkerCount() {
    return workerCount;
  }

  public void setWorkerCount(int workerCount) {
    this.workerCount = workerCount;
  }

  public int getDetailsCount() {
    return detailsCount;
  }

  public void setDetailsCount(int detailsCount) {
    this.detailsCount = detailsCount;
  }

  public List<ProductionCenter> getProductionCenters() {
    return productionCenters;
  }

  public void setProductionCenters(
      List<ProductionCenter> productionCenters) {
    this.productionCenters = productionCenters;
  }

  public List<Connection> getConnections() {
    return connections;
  }

  public void setConnections(List<Connection> connections) {
    this.connections = connections;
  }
}