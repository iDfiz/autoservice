package main.java.ru.autoservice.model;

/** Клиент автосервиса. */
public final class Client extends Person {
    private final String email;

    /** Создаёт клиента. */
    public Client(String fullName, String phone, String email) {
        super(fullName, phone);
        if (email == null || !email.contains("@")) throw new IllegalArgumentException("Некорректный email");
        this.email = email.trim();
    }
    /** @return email клиента */
    public String getEmail() { return email; }
    @Override public String getRole() { return "Клиент"; }
    @Override public String toString() { return getFullName() + " (" + getPhone() + ", " + email + ")"; }
}
