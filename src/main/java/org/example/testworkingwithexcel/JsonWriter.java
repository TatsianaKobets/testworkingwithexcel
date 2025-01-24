package org.example.testworkingwithexcel;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class JsonWriter {

  ObjectMapper mapper = new ObjectMapper();
  int workerCount = 0;
  int detailsCount = 0;

  public JsonWriter() throws IOException {
  }

  public void writeToJson() throws IOException {

    ObjectMapper mapper = new ObjectMapper();
    ObjectNode jsonRoot = (ObjectNode) mapper.readTree(new File("output.json"));
    // Iterate over the JSON object's fields
    for (Iterator<String> fieldNames = jsonRoot.fieldNames(); fieldNames.hasNext(); ) {
      String fieldName = fieldNames.next();
      JsonNode fieldValue = jsonRoot.get(fieldName);

      // If the field value is an array, iterate over its elements
      if (fieldValue.isArray()) {
        ArrayNode jsonArray = (ArrayNode) fieldValue;
        for (int i = 0; i < jsonArray.size(); i++) {
          ObjectNode jsonObject = (ObjectNode) jsonArray.get(i);
          // Process the JSON object
          if (jsonObject.has("column1") && jsonObject.has("column2")) {
            if (jsonObject.get("column1").asText().equals("workerCount") && jsonObject.get(
                "column2").asText().equals("detailsCount")) {
              // Если ключи совпадают, берем значения из следующего объекта
              ObjectNode nextObject = (ObjectNode) jsonArray.get(i + 1);
              double workerCount = nextObject.get("column1").asDouble();
              double detailsCount = nextObject.get("column2").asDouble();
              System.out.println("workerCount: " + workerCount);
              System.out.println("detailsCount: " + detailsCount);
              break;
            }
          }
        }
      }
    }
  }

}