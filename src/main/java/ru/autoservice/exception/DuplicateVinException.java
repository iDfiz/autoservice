package main.java.ru.autoservice.exception;

/** Исключение при регистрации автомобиля с уже существующим VIN. */
public class DuplicateVinException extends Exception {
    /** Создаёт исключение с VIN-дубликатом. */
    public DuplicateVinException(String message) { super(message); }
}
