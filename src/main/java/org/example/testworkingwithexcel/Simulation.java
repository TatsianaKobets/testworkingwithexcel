package org.example.testworkingwithexcel;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * моделирования процесса обработки деталей на производственных центрах с учетом заданного общего количества сотрудников.
 */
public class Simulation {
//метод runSimulation в классе Simulation для запуска симуляции и записи результатов в CSV-файл.
// Список производственных центров
  private List<ProductionCenter> productionCenters;
  // Список сотрудников
  private List<Employee> employees;
  // Общее количество сотрудников
  private int totalEmployees;
  // Время симуляции
  private double time;

  public Simulation(List<ProductionCenter> productionCenters, List<Employee> employees, int totalEmployees) {
    this.productionCenters = productionCenters;
    this.employees = employees;
    this.totalEmployees = totalEmployees;
    this.time = 0;
  }

  // Метод запуска симуляции
  public void runSimulation() throws IOException {
    // Создание файла для записи результатов
    FileWriter writer = new FileWriter("result.csv");

    // Запись заголовков в файл
    writer.write("Time, ProductionCenter, WorkersCount, BufferCount\n");

    // Основной цикл симуляции
    while (time < 1000) { // симуляция длится 1000 минут
      // Обработка каждым производственным центром деталей
      for (ProductionCenter productionCenter : productionCenters) {
        // Обработка деталей в буфере
        for (Part part : productionCenter.getBuffer()) {
          // Если деталь уже обработана, то она удаляется из буфера
          if (part.isProcessed()) {
            productionCenter.removePart(part);
          } else {
            // Если деталь не обработана, то она обрабатывается сотрудниками
            for (Employee employee : productionCenter.getEmployees()) {
              // Если сотрудник свободен, то он начинает обработку детали
              if (employee.isFree()) {
                employee.assignToProductionCenter(productionCenter);
                part.setCurrentProductionCenter(productionCenter);
                // Обработка детали занимает определенное время
                part.setProcessingTime(part.getProcessingTime() - 1);
                // Если деталь обработана, то она помечается как обработанная
                if (part.getProcessingTime() <= 0) {
                  part.setCurrentProductionCenter(null);
                  employee.freeFromProductionCenter();
                }
              }
            }
          }
        }
      }

      // Запись результатов в файл
      for (ProductionCenter productionCenter : productionCenters) {
        writer.write(time + ", " + productionCenter.getName() + ", " + productionCenter.getWorkersCount() + ", " + productionCenter.getBuffer().size() + "\n");
      }

      // Увеличение времени симуляции
      time++;
    }

    // Закрытие файла
    writer.close();
  }
}
