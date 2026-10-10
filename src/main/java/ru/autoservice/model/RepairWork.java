package main.java.ru.autoservice.model;

/** Работа с ценой одного нормо-часа и количеством нормо-часов. */
public record RepairWork(WorkType type, double hourlyRate, double laborHours) implements PriceCalculable {
    /** Проверяет корректность стоимости и трудоёмкости. */
    public RepairWork {
        if (type == null) throw new IllegalArgumentException("Тип работы обязателен");
        if (!Double.isFinite(hourlyRate) || hourlyRate < 0) throw new IllegalArgumentException("Цена нормо-часа не может быть отрицательной");
        if (!Double.isFinite(laborHours) || laborHours <= 0) throw new IllegalArgumentException("Нормо-часы должны быть больше нуля");
    }
    /** @return стоимость работы: ставка умножается на нормо-часы */
    @Override public double calculatePrice() { return hourlyRate * laborHours; }
}
