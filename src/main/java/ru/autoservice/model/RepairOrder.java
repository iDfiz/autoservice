package main.java.ru.autoservice.model;

import main.java.ru.autoservice.exception.InvalidTransitionException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Заявка на ремонт автомобиля. */
public final class RepairOrder implements PriceCalculable {
    private final long id;
    private final Car car;
    private final Client client;
    private final LocalDate createdDate;
    private final LocalDate promisedDate;
    private final List<RepairWork> works = new ArrayList<>();
    private final List<StatusHistory> history = new ArrayList<>();
    private OrderStatus status;
    private LocalDateTime repairStartedAt;
    private LocalDateTime repairFinishedAt;

    /** Создаёт заявку. */
    public RepairOrder(long id, Car car, Client client, LocalDate createdDate, LocalDate promisedDate) {
        if (id <= 0) throw new IllegalArgumentException("ID заявки должен быть положительным");
        this.id = id; this.car = Objects.requireNonNull(car); this.client = Objects.requireNonNull(client);
        this.createdDate = Objects.requireNonNull(createdDate); this.promisedDate = Objects.requireNonNull(promisedDate);
        if (promisedDate.isBefore(createdDate)) throw new IllegalArgumentException("Дата выдачи не может быть раньше даты создания");
        this.status = OrderStatus.ACCEPTED;
        history.add(new StatusHistory(null, status, LocalDateTime.now(), "Заявка создана"));
    }
    /** @return идентификатор */
    public long getId() { return id; }
    /** @return автомобиль */
    public Car getCar() { return car; }
    /** @return клиент */
    public Client getClient() { return client; }
    /** @return дата создания */
    public LocalDate getCreatedDate() { return createdDate; }
    /** @return обещанная дата выдачи */
    public LocalDate getPromisedDate() { return promisedDate; }
    /** @return текущий статус */
    public OrderStatus getStatus() { return status; }
    /** @return неизменяемая копия списка работ */
    public List<RepairWork> getWorks() { return List.copyOf(works); }
    /** @return неизменяемая копия журнала статусов */
    public List<StatusHistory> getHistory() { return List.copyOf(history); }
    /** Добавляет работу до выдачи или отмены заявки. */
    public void addWork(RepairWork work) {
        if (status == OrderStatus.ISSUED || status == OrderStatus.CANCELLED) throw new IllegalStateException("Нельзя менять завершённую заявку");
        works.add(Objects.requireNonNull(work));
    }
    /** @return стоимость всех работ */
    @Override public double calculatePrice() { return works.stream().mapToDouble(RepairWork::calculatePrice).sum(); }

    /** Меняет статус, проверяя машину состояний, и записывает переход в журнал. */
    public void transitionTo(OrderStatus next, String comment) throws InvalidTransitionException {
        if (!status.canTransitionTo(next))
            throw new InvalidTransitionException("Переход из «" + status.getTitle() + "» в «" + (next == null ? "null" : next.getTitle()) + "» запрещён");
        OrderStatus previous = status;
        status = next;
        LocalDateTime now = LocalDateTime.now();
        history.add(new StatusHistory(previous, next, now, comment));
        if (next == OrderStatus.REPAIR) repairStartedAt = now;
        if (next == OrderStatus.READY) repairFinishedAt = now;
    }
    /** @return длительность ремонта; пусто, если ремонт ещё не завершён */
    public java.util.Optional<Duration> getRepairDuration() {
        if (repairStartedAt == null || repairFinishedAt == null) return java.util.Optional.empty();
        return java.util.Optional.of(Duration.between(repairStartedAt, repairFinishedAt));
    }
    /** @return true, если заявка ещё не выдана и обещанная дата уже прошла */
    public boolean isOverdue(LocalDate date) {
        Objects.requireNonNull(date);
        return date.isAfter(promisedDate) && status != OrderStatus.ISSUED && status != OrderStatus.CANCELLED;
    }
    @Override public String toString() {
        return "Заявка #" + id + " | " + car + " | " + status.getTitle() + " | " + String.format("%.2f ₽", calculatePrice());
    }
}
