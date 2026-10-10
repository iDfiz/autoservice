package test.model;

import main.java.ru.autoservice.model.Car;
import main.java.ru.autoservice.model.Client;
import main.java.ru.autoservice.model.Vin;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CarTest {
    private Client client() { return new Client("Тест Клиент", "+70000000000", "test@example.com"); }
    private Vin vin() { return new Vin("WVWZZZ1JZ3W000001"); }

    @Test void vinIsNormalized() { assertEquals("WVWZZZ1JZ3W000001", new Vin(" wvwzzz1jz3w000001 ").value()); }
    @Test void vinRejectsWrongLength() { assertThrows(IllegalArgumentException.class, () -> new Vin("ABC")); }
    @Test void vinRejectsForbiddenLetters() { assertThrows(IllegalArgumentException.class, () -> new Vin("WVWZZZ1JZ3W00000I")); }
    @Test void carsWithSameVinAreEqual() {
        var a = new Car(vin(), "VW", "Golf", 2020, client());
        var b = new Car(vin(), "Other", "Model", 2018, client());
        assertEquals(a, b);
    }
    @Test void equalCarsHaveSameHashCode() {
        var a = new Car(vin(), "VW", "Golf", 2020, client());
        var b = new Car(vin(), "VW", "Golf", 2020, client());
        assertEquals(a.hashCode(), b.hashCode());
    }
    @Test void carRejectsInvalidYear() {
        assertThrows(IllegalArgumentException.class, () -> new Car(vin(), "VW", "Golf", 1000, client()));
    }
    @Test void carRequiresOwner() {
        assertThrows(NullPointerException.class, () -> new Car(vin(), "VW", "Golf", 2020, null));
    }
    @Test void clientRequiresValidEmail() {
        assertThrows(IllegalArgumentException.class, () -> new Client("Test", "123", "bad-email"));
    }
}
