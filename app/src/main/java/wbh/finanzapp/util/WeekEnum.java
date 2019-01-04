package wbh.finanzapp.util;

public enum WeekEnum {

    SUNDAY("Sunday", 1),
    MONDAY("Monday", 2),
    THUESDAY("Thuesday", 3),
    WEDNESDAY("Wednesday", 4),
    THURSDAY("Thursday", 5),
    FRIDAY("Friday", 6),
    SATURDAY("Saturday", 7);

    private final String key;
    private final Integer value;

    WeekEnum(String key, Integer value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Integer getValue() {
        return value;
    }
}
