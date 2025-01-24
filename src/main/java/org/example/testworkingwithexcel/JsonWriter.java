package org.example.testworkingwithexcel;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonWriter {

  ObjectMapper mapper = new ObjectMapper();
  int workerCount = 0;
  int detailsCount = 0;

  public JsonWriter() throws IOException {
  }

  public void writeToJson() throws IOException {
// Создание списка производственных центров
    List<ProductionCenter> productionCenters = new ArrayList<>();

    // Создание списка сотрудников
    List<Employee> employees = new ArrayList<>();
    // Чтение данных из файла output.json
    ObjectMapper mapper = new ObjectMapper();
    ObjectNode jsonRoot = (ObjectNode) mapper.readTree(new File("output.json"));



    // Write the updated JSON object to the output file
    mapper.writeValue(new File("output.json"), jsonRoot);
  }
}