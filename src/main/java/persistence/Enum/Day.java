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

    public static Day getValue(String day) {

        if (day.equals("월") || day.equals("MON")){
            return MON;
        } else if(day.equals("화") || day.equals("TUE")) {
            return TUE;
        } else if(day.equals("수") || day.equals("WED")) {
            return WED;
        } else if(day.equals("목") || day.equals("THU")) {
            return THU;
        } else if(day.equals("금") || day.equals("FRI")) {
            return FRI;
        } else
            return null;
    }
}