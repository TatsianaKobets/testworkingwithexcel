package org.example.testworkingwithexcel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.io.IOException;

public class JsonWriter {

  ObjectMapper mapper = new ObjectMapper();
  int workerCount = 0;
  int detailsCount = 0;

  public JsonWriter() throws IOException {
  }

  public void writeToJson() throws IOException {

    ObjectMapper mapper = new ObjectMapper();
    ArrayNode jsonArray = (ArrayNode) mapper.readTree(new File("output.json"));

    double workerCount = 0;
    double detailsCount = 0;

    for (int i = 0; i < jsonArray.size(); i++) {
      ObjectNode jsonObject = (ObjectNode) jsonArray.get(i);
      if (jsonObject.has("column1") && jsonObject.has("column2")) {
        if (jsonObject.get("column1").asText().equals("workerCount") && jsonObject.get("column2").asText().equals("detailsCount")) {
          // Если ключи совпадают, берем значения из следующего объекта
          ObjectNode nextObject = (ObjectNode) jsonArray.get(i + 1);
          workerCount = nextObject.get("column1").asDouble();
          detailsCount = nextObject.get("column2").asDouble();
          break;
        }
      }
    }

    System.out.println("workerCount: " + workerCount);
    System.out.println("detailsCount: " + detailsCount);
    }
}