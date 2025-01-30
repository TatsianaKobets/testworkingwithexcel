package org.example.testworkingwithexcel.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.example.testworkingwithexcel.entity.Employee;
import org.example.testworkingwithexcel.entity.Part;
import org.example.testworkingwithexcel.entity.ProductionCenter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SimulationTest {

  private Simulation simulation;
  private List<ProductionCenter> productionCenters;
  private List<Employee> employees;
  private List<Connection> connections;

  @BeforeEach
  void setUp() {
    productionCenters = new ArrayList<>();
    employees = new ArrayList<>();
    connections = new ArrayList<>();
  }

  @Test
  void testRedistributeEmployees() {
    // Arrange
    ProductionCenter source = mock(ProductionCenter.class);
    ProductionCenter target = mock(ProductionCenter.class);
    Employee employee = mock(Employee.class);

    // Настройка разной нагрузки
    when(source.getBuffer()).thenReturn(List.of(new Part(0, 0.2), new Part(1, 0.2), new Part(2, 0.2)));
    when(target.getBuffer()).thenReturn(List.of(new Part(3, 0.2)));

    when(source.getEmployees()).thenReturn(List.of(employee));
    when(employee.isFree()).thenReturn(true);
    when(target.getEmployees()).thenReturn(new ArrayList<>());
    when(target.getMaxWorkersCount()).thenReturn(2.0);

    productionCenters.add(target); // Менее загруженный
    productionCenters.add(source); // Более загруженный
    employees.add(employee);

    simulation = new Simulation(
        productionCenters, employees, 5, 1, connections
    );

    // Act
    simulation.redistributeEmployees();

    // Assert
    verify(source).removeEmployee(employee);
    verify(target).addEmployee(employee);
  }

  @Test
  void testCanMoveEmployeeWhenPossible() {
    // Arrange
    ProductionCenter source = mock(ProductionCenter.class);
    ProductionCenter target = mock(ProductionCenter.class);

    // Создаем мок сотрудника и настраиваем его
    Employee freeEmployee = mock(Employee.class);
    when(freeEmployee.isFree()).thenReturn(true); // Убедимся, что сотрудник свободен

    // Инициализируем simulation
    simulation = new Simulation(
        new ArrayList<>(), // productionCenters (может быть пустым для этого теста)
        new ArrayList<>(), // employees (не используется в этом тесте)
        0,                 // detailsCount (не используется)
        0,                 // workerCount (не используется)
        new ArrayList<>()  // connections (не используется)
    );

    // Настраиваем моки
    when(source.getEmployees()).thenReturn(
        List.of(freeEmployee)); // Возвращаем свободного сотрудника
    when(target.getEmployees()).thenReturn(new ArrayList<>()); // Целевой центр пуст
    when(target.getMaxWorkersCount()).thenReturn(2.0); // Максимум свободных мест - 2

    // Act
    boolean result = simulation.canMoveEmployee(source, target);

    // Assert
    assertTrue(result); // Теперь это должно вернуть true
  }

  @Test
  void testMoveEmployee() {
    // Arrange
    ProductionCenter source = mock(ProductionCenter.class);
    ProductionCenter target = mock(ProductionCenter.class);
    Employee employee = new Employee(1);

    when(source.getEmployees()).thenReturn(List.of(employee));

    simulation = new Simulation(
        productionCenters, employees, 5, 1, connections
    );

    // Act
    simulation.moveEmployee(source, target);

    // Assert
    verify(source).removeEmployee(employee);
    verify(target).addEmployee(employee);
    assertEquals(target, employee.getCurrentCenter());
  }

  @Test
  void testIsSimulationFinishedWhenAllDone() {
    // Arrange
    ProductionCenter pc = mock(ProductionCenter.class);
    when(pc.getBuffer()).thenReturn(new ArrayList<>());
    when(pc.getEmployees()).thenReturn(new ArrayList<>());
    when(pc.getProcessingParts()).thenReturn("");

    productionCenters.add(pc);
    simulation = new Simulation(
        productionCenters, employees, 0, 0, connections
    );

    // Act
    boolean result = simulation.isSimulationFinished();

    // Assert
    assertTrue(result);
  }

  @Test
  void testProcessStep() throws IOException {
    // Arrange
    ProductionCenter pc = mock(ProductionCenter.class);
    Connection conn = mock(Connection.class);

    productionCenters.add(pc);
    connections.add(conn);

    simulation = new Simulation(
        productionCenters, employees, 5, 0, connections
    );

    // Act
    simulation.processStep();

    // Assert
    verify(pc).processParts(anyDouble());
    verify(conn).getBuffer();
  }

  @Test
  void testDistributeEmployeesToCenters() {
    // Arrange
    ProductionCenter pc = new ProductionCenter("1", "Test", 1.0, 2.0);
    employees.add(new Employee(1));
    employees.add(new Employee(2));
    productionCenters.add(pc);

    simulation = new Simulation(
        productionCenters, employees, 0, 2, connections
    );

    // Act
    simulation.distributeEmployeesToCenters();

    // Assert
    assertEquals(2, pc.getEmployees().size());
    assertEquals(0, employees.size());
  }

  @Test
  void testGetParts() {
    // Arrange
    simulation = new Simulation(
        productionCenters, employees, 3, 0, connections
    );

    // Act
    List<Part> parts = simulation.getParts(3);

    // Assert
    assertEquals(3, parts.size());
    assertEquals(0, parts.get(0).getId());
    assertEquals(0.2, parts.get(0).getProcessingTime());
  }
}
