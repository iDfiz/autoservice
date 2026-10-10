package main.java.ru.autoservice.model;

/** Контракт объектов, стоимость которых можно вычислить. */
public interface PriceCalculable {
    /** @return итоговая стоимость в рублях */
    double calculatePrice();
}
