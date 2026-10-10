package main.java.ru.autoservice.model;

/** Статусы заявки и правила переходов между ними. */
public enum OrderStatus {
    ACCEPTED("Принята"), DIAGNOSTICS("Диагностика"), APPROVAL("Согласование"),
    REPAIR("Ремонт"), READY("Готова"), ISSUED("Выдана"), CANCELLED("Отменена");

    private final String title;
    OrderStatus(String title) { this.title = title; }
    /** @return название статуса */
    public String getTitle() { return title; }

    /** Проверяет, разрешён ли переход из текущего статуса в следующий. */
    public boolean canTransitionTo(OrderStatus next) {
        if (next == null || next == this) return false;
        return switch (this) {
            case ACCEPTED -> next == DIAGNOSTICS || next == CANCELLED;
            case DIAGNOSTICS -> next == APPROVAL || next == CANCELLED;
            case APPROVAL -> next == REPAIR || next == CANCELLED;
            case REPAIR -> next == READY;
            case READY -> next == ISSUED;
            case ISSUED, CANCELLED -> false;
        };
    }
}
