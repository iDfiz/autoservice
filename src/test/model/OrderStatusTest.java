package test.model;

import main.java.ru.autoservice.model.OrderStatus;
import main.java.ru.autoservice.model.RepairWork;
import main.java.ru.autoservice.model.WorkType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OrderStatusTest {
    @Test void acceptedCanGoToDiagnostics() { assertTrue(OrderStatus.ACCEPTED.canTransitionTo(OrderStatus.DIAGNOSTICS)); }
    @Test void acceptedCanBeCancelled() { assertTrue(OrderStatus.ACCEPTED.canTransitionTo(OrderStatus.CANCELLED)); }
    @Test void acceptedCannotGoDirectlyToRepair() { assertFalse(OrderStatus.ACCEPTED.canTransitionTo(OrderStatus.REPAIR)); }
    @Test void issuedIsTerminal() { assertFalse(OrderStatus.ISSUED.canTransitionTo(OrderStatus.REPAIR)); }
    @Test void cancelledIsTerminal() { assertFalse(OrderStatus.CANCELLED.canTransitionTo(OrderStatus.ACCEPTED)); }
    @Test void nullTransitionIsInvalid() { assertFalse(OrderStatus.REPAIR.canTransitionTo(null)); }
    @Test void workPriceIsCalculated() { assertEquals(3600, new RepairWork(WorkType.OIL_CHANGE, 1800, 2).calculatePrice()); }
    @Test void zeroHoursRejected() { assertThrows(IllegalArgumentException.class, () -> new RepairWork(WorkType.ENGINE, 2000, 0)); }
}
