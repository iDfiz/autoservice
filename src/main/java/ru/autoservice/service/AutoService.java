package main.java.ru.autoservice.service;

import main.java.ru.autoservice.exception.DuplicateVinException;
import main.java.ru.autoservice.model.*;
import java.time.LocalDate;
import java.util.*;

/** Фасад для управления автомобилями и заявками автосервиса. */
public final class AutoService {
    private final Map<Vin, Car> cars = new LinkedHashMap<>();
    private final Map<Long, RepairOrder> orders = new LinkedHashMap<>();

    /** Регистрирует автомобиль; повтор VIN вызывает осмысленное исключение. */
    public void registerCar(Car car) throws DuplicateVinException {
        Objects.requireNonNull(car);
        if (cars.containsKey(car.getVin())) throw new DuplicateVinException("Автомобиль с VIN " + car.getVin().value() + " уже зарегистрирован");
        cars.put(car.getVin(), car);
    }
    /** Ищет автомобиль по VIN. */
    public Optional<Car> findCarByVin(Vin vin) { return Optional.ofNullable(cars.get(Objects.requireNonNull(vin))); }
    /** Добавляет заявку с уникальным ID. */
    public void addOrder(RepairOrder order) {
        Objects.requireNonNull(order);
        if (orders.putIfAbsent(order.getId(), order) != null) throw new IllegalArgumentException("Заявка с таким ID уже существует");
    }
    /** Ищет заявку по ID. */
    public Optional<RepairOrder> findOrder(long id) { return Optional.ofNullable(orders.get(id)); }
    /** Возвращает копию всех заявок. */
    public List<RepairOrder> getAllOrders() { return List.copyOf(orders.values()); }
    /** Возвращает заявки, находящиеся в работе на указанную дату. */
    public List<RepairOrder> getOrdersInProgress(LocalDate date) {
        Objects.requireNonNull(date);
        return orders.values().stream()
                .filter(o -> !o.getCreatedDate().isAfter(date))
                .filter(o -> o.getStatus() == OrderStatus.DIAGNOSTICS || o.getStatus() == OrderStatus.APPROVAL || o.getStatus() == OrderStatus.REPAIR || o.getStatus() == OrderStatus.READY)
                .toList();
    }
    /** Возвращает просроченные заявки на указанную дату. */
    public List<RepairOrder> getOverdueOrders(LocalDate date) {
        return orders.values().stream().filter(o -> o.isOverdue(date)).toList();
    }
    /** @return средняя длительность завершённых ремонтов в секундах для типа работы */
    public OptionalDouble getAverageRepairSeconds(WorkType type) {
        Objects.requireNonNull(type);
        var durations = orders.values().stream()
                .filter(o -> o.getWorks().stream().anyMatch(w -> w.type() == type))
                .map(RepairOrder::getRepairDuration).flatMap(Optional::stream)
                .mapToLong(java.time.Duration::getSeconds);
        double[] values = durations.asDoubleStream().toArray();
        return values.length == 0 ? OptionalDouble.empty() : OptionalDouble.of(Arrays.stream(values).average().orElseThrow());
    }
}
