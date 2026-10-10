package main.java.ru.autoservice.model;

import java.util.Objects;

/** Автомобиль клиента; равенство определяется исключительно VIN. */
public final class Car {
    private final Vin vin;
    private final String brand;
    private final String model;
    private final int year;
    private final Client owner;

    /** Создаёт автомобиль. */
    public Car(Vin vin, String brand, String model, int year, Client owner) {
        this.vin = Objects.requireNonNull(vin, "VIN обязателен");
        if (brand == null || brand.isBlank() || model == null || model.isBlank())
            throw new IllegalArgumentException("Марка и модель обязательны");
        if (year < 1886 || year > java.time.Year.now().getValue() + 1)
            throw new IllegalArgumentException("Некорректный год выпуска");
        this.brand = brand.trim(); this.model = model.trim(); this.year = year;
        this.owner = Objects.requireNonNull(owner, "Владелец обязателен");
    }
    /** @return VIN */
    public Vin getVin() { return vin; }
    /** @return марка */
    public String getBrand() { return brand; }
    /** @return модель */
    public String getModel() { return model; }
    /** @return год выпуска */
    public int getYear() { return year; }
    /** @return владелец */
    public Client getOwner() { return owner; }
    @Override public boolean equals(Object other) { return this == other || other instanceof Car car && vin.equals(car.vin); }
    @Override public int hashCode() { return vin.hashCode(); }
    @Override public String toString() { return brand + " " + model + " (" + year + "), VIN=" + vin.value(); }
}
