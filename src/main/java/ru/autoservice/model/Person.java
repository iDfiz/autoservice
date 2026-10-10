package main.java.ru.autoservice.model;

/** Общие данные человека в системе автосервиса. */
public abstract class Person {
    private final String fullName;
    private final String phone;

    /**
     * Создаёт человека.
     * @param fullName имя и фамилия
     * @param phone контактный телефон
     */
    protected Person(String fullName, String phone) {
        if (fullName == null || fullName.isBlank()) throw new IllegalArgumentException("Имя не может быть пустым");
        if (phone == null || phone.isBlank()) throw new IllegalArgumentException("Телефон не может быть пустым");
        this.fullName = fullName.trim();
        this.phone = phone.trim();
    }
    /** @return полное имя */
    public String getFullName() { return fullName; }
    /** @return телефон */
    public String getPhone() { return phone; }
    /** @return роль человека в системе */
    public abstract String getRole();
}
