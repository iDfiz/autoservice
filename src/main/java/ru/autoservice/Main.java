package main.java.ru.autoservice;

import main.java.ru.autoservice.exception.*;
import main.java.ru.autoservice.model.*;
import main.java.ru.autoservice.exception.DuplicateVinException;
import main.java.ru.autoservice.model.Car;
import main.java.ru.autoservice.model.Client;
import main.java.ru.autoservice.model.Employee;
import main.java.ru.autoservice.model.Vin;
import main.java.ru.autoservice.service.AutoService;
import java.time.LocalDate;

/** Консольная демонстрация операций модели автосервиса. */
public class Main {
    public static void main(String[] args) {
        var service = new AutoService();
        var client = new Client("Иван Петров", "+7-900-000-00-01", "ivan@example.com");
        var employee = new Employee("Анна Смирнова", "+7-900-000-00-02", "Механик");
        System.out.println("=== Демонстрация модели автосервиса ===");
        System.out.println("Роль клиента: " + client.getRole() + "; роль сотрудника: " + employee.getRole());

        // Заполняем модель минимум 30 объектами: 10 клиентов, 10 машин, 10 заявок.
        for (int i = 1; i <= 10; i++) {
            var c = new Client("Клиент " + i, "+7-900-100-00-" + String.format("%02d", i), "client" + i + "@example.com");
            var vin = new Vin(String.format("WVWZZZ1JZ3W%06d", i));
            Car car = new Car(vin, i % 2 == 0 ? "Toyota" : "Volkswagen", "Модель-" + i, 2015 + i % 9, c);
            try { service.registerCar(car); } catch (DuplicateVinException e) { System.err.println("Ошибка регистрации: " + e.getMessage()); }
            RepairOrder order = new RepairOrder(i, car, c, LocalDate.now().minusDays(i), LocalDate.now().plusDays(5 - i % 3));
            order.addWork(new RepairWork(WorkType.OIL_CHANGE, 1800, 1.0 + (i % 3) * 0.5));
            order.addWork(new RepairWork(WorkType.DIAGNOSTICS, 1500, 0.5));
            service.addOrder(order);
            try {
                order.transitionTo(OrderStatus.DIAGNOSTICS, "Первичная проверка");
                if (i % 3 != 0) {
                    order.transitionTo(OrderStatus.APPROVAL, "Стоимость согласована");
                    order.transitionTo(OrderStatus.REPAIR, "Начало ремонта");
                    if (i % 2 == 0) order.transitionTo(OrderStatus.READY, "Работы завершены");
                }
            } catch (InvalidTransitionException e) {
                System.err.println("Не удалось изменить статус заявки #" + i + ": " + e.getMessage());
            }
        }

        System.out.println("\n--- Все заявки и стоимость ---");
        service.getAllOrders().forEach(o -> System.out.println(o + " | клиент: " + o.getClient().getFullName()));
        System.out.println("\n--- Поиск автомобиля по VIN ---");
        Vin firstVin = new Vin("WVWZZZ1JZ3W000001");
        System.out.println(service.findCarByVin(firstVin).orElse(null));
        System.out.println("\n--- Заявки в работе на сегодня ---");
        service.getOrdersInProgress(LocalDate.now()).forEach(System.out::println);
        System.out.println("\n--- Просроченные заявки ---");
        service.getOverdueOrders(LocalDate.now()).forEach(System.out::println);
        System.out.println("\n--- Демонстрация исключения ---");
        try {
            service.registerCar(service.findCarByVin(firstVin).orElseThrow());
        } catch (DuplicateVinException e) {
            System.out.println("Ожидаемая ошибка обработана: " + e.getMessage());
        }
        System.out.println("\nКоличество заявок: " + service.getAllOrders().size());
        System.out.println("Среднее время ремонта для замены масла (сек.): " + service.getAverageRepairSeconds(WorkType.OIL_CHANGE).orElse(-1));
    }
}
