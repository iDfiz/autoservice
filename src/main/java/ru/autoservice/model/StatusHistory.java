package main.java.ru.autoservice.model;

import java.time.LocalDateTime;

/** Запись журнала изменения статуса заявки. */
public record StatusHistory(OrderStatus from, OrderStatus to, LocalDateTime changedAt, String comment) {
    /** Проверяет полноту записи истории. */
    public StatusHistory {
        if (to == null || changedAt == null) throw new IllegalArgumentException("Статус и дата обязательны");
        comment = comment == null ? "" : comment;
    }
}
