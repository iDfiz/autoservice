package main.java.ru.autoservice.model;

/** Тип выполняемой ремонтной работы. */
public enum WorkType {
    DIAGNOSTICS("Диагностика"), ENGINE("Ремонт двигателя"), BRAKES("Тормозная система"),
    OIL_CHANGE("Замена масла"), SUSPENSION("Подвеска"), ELECTRICAL("Электрика"), BODYWORK("Кузовной ремонт");
    private final String title;
    WorkType(String title) { this.title = title; }
    /** @return читаемое название типа работы */
    public String getTitle() { return title; }
}
