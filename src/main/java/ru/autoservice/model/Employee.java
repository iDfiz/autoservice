package main.java.ru.autoservice.model;

/** Сотрудник автосервиса. */
public final class Employee extends Person {
    private final String position;

    /** Создаёт сотрудника с должностью. */
    public Employee(String fullName, String phone, String position) {
        super(fullName, phone);
        if (position == null || position.isBlank()) throw new IllegalArgumentException("Должность обязательна");
        this.position = position.trim();
    }
    /** @return должность сотрудника */
    public String getPosition() { return position; }
    @Override public String getRole() { return "Сотрудник: " + position; }
}
