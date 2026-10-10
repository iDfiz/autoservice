package main.java.ru.autoservice.exception;

/** Исключение при попытке выполнить недопустимый переход статуса заявки. */
public class InvalidTransitionException extends Exception {
    /** Создаёт исключение с пояснением ошибки. */
    public InvalidTransitionException(String message) { super(message); }
}
