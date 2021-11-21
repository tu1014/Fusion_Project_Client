package persistence.Enum;

public enum Day {

    MON("월"),
    TUE("화"),
    WED("수"),
    THU("목"),
    FRI("금");

    private final String label;

    Day(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}