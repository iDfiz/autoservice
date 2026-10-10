package main.java.ru.autoservice.model;

import java.util.Locale;

/** VIN автомобиля. Используется как неизменяемый идентификатор с корректными equals/hashCode. */
public record Vin(String value) {
    /** Проверяет формат VIN: 17 символов, без I, O и Q. */
    public Vin {
        if (value == null) throw new IllegalArgumentException("VIN не может быть null");
        value = value.trim().toUpperCase(Locale.ROOT);
        if (!value.matches("[A-HJ-NPR-Z0-9]{17}")) throw new IllegalArgumentException("VIN должен содержать 17 допустимых символов");
    }
}
