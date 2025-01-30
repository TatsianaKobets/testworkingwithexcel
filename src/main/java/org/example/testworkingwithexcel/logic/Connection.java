package org.example.testworkingwithexcel.logic;

import java.util.ArrayList;
import java.util.List;
import org.example.testworkingwithexcel.entity.Part;
import org.example.testworkingwithexcel.entity.ProductionCenter;

/**
 * Representation of connections between production centers.
 */
public class Connection {

  private ProductionCenter from;
  private ProductionCenter to;
  private List<Part> buffer = new ArrayList<>();

  public Connection(ProductionCenter from, ProductionCenter to) {
    this.from = from;
    this.to = to;
  }

  public void addPart(Part part) {
    buffer.add(part);
    System.out.printf("Деталь %d в пути: %s -> %s\n",
        part.getId(), from.getName(), to.getName());
  }

  public void removePart(Part part) {
    buffer.remove(part);
  }

  public ProductionCenter getFrom() {
    return from;
  }

  public ProductionCenter getTo() {
    return to;
  }

  public List<Part> getBuffer() {
    return buffer;
  }
}

