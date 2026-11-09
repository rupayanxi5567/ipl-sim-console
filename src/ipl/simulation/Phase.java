package ipl.simulation;

public enum Phase {
    POWERPLAY, MIDDLE, DEATH;

    public static Phase ofOver(int over) {
        if (over < 6) {
            return Phase.POWERPLAY;
        }
        if (over < 15) {
            return Phase.MIDDLE;
        }
        return Phase.DEATH;
    }
}