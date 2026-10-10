package test.service;

import main.java.ru.autoservice.model.Client;
import main.java.ru.autoservice.service.AutoService;
import org.junit.jupiter.api.Test;
import main.java.ru.autoservice.exception.*;
import main.java.ru.autoservice.model.*;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class AutoServiceTest {
    private Client client() { return new Client("Тест Клиент", "+70000000000", "test@example.com"); }
    private Car car(String vin) { return new Car(new Vin(vin), "Toyota", "Corolla", 2020, client()); }
    private RepairOrder order(long id, Car car) {
        return new RepairOrder(id, car, car.getOwner(), LocalDate.now().minusDays(2), LocalDate.now().plusDays(2));
    }
    @Test void registersAndFindsCar() throws Exception {
        var service = new AutoService(); var c = car("WVWZZZ1JZ3W000001");
        service.registerCar(c); assertEquals(c, service.findCarByVin(c.getVin()).orElseThrow());
    }
    @Test void duplicateVinThrowsMeaningfulException() throws Exception {
        var service = new AutoService(); service.registerCar(car("WVWZZZ1JZ3W000001"));
        var error = assertThrows(DuplicateVinException.class, () -> service.registerCar(car("WVWZZZ1JZ3W000001")));
        assertTrue(error.getMessage().contains("VIN"));
    }
    @Test void missingCarReturnsEmpty() { assertTrue(new AutoService().findCarByVin(new Vin("WVWZZZ1JZ3W000001")).isEmpty()); }
    @Test void addsAndFindsOrder() {
        var service = new AutoService(); var order = order(1, car("WVWZZZ1JZ3W000001"));
        service.addOrder(order); assertEquals(order, service.findOrder(1).orElseThrow());
    }
    @Test void duplicateOrderIdRejected() {
        var service = new AutoService(); service.addOrder(order(1, car("WVWZZZ1JZ3W000001")));
        assertThrows(IllegalArgumentException.class, () -> service.addOrder(order(1, car("WVWZZZ1JZ3W000002"))));
    }
    @Test void invalidTransitionThrows() {
        var order = order(1, car("WVWZZZ1JZ3W000001"));
        assertThrows(InvalidTransitionException.class, () -> order.transitionTo(OrderStatus.REPAIR, "Нельзя перескочить этап"));
    }
    @Test void historyIsDefensivelyCopied() throws Exception {
        var order = order(1, car("WVWZZZ1JZ3W000001"));
        assertThrows(UnsupportedOperationException.class, () -> order.getHistory().clear());
    }
    @Test void priceSumsAllWorks() {
        var order = order(1, car("WVWZZZ1JZ3W000001"));
        order.addWork(new RepairWork(WorkType.OIL_CHANGE, 1000, 2));
        order.addWork(new RepairWork(WorkType.BRAKES, 1500, 1.5));
        assertEquals(4250, order.calculatePrice());
    }
    @Test void cannotAddWorkToCancelledOrder() throws Exception {
        var order = order(1, car("WVWZZZ1JZ3W000001"));
        order.transitionTo(OrderStatus.CANCELLED, "Клиент отказался");
        assertThrows(IllegalStateException.class, () -> order.addWork(new RepairWork(WorkType.OIL_CHANGE, 1000, 1)));
    }
    @Test void overdueOrderIsDetected() throws Exception {
        var car = car("WVWZZZ1JZ3W000001");
        var order = new RepairOrder(1, car, car.getOwner(), LocalDate.now().minusDays(5), LocalDate.now().minusDays(1));
        assertTrue(order.isOverdue(LocalDate.now()));
    }
}
