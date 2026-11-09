package ipl.simulation;

public enum WicketType {
    BOWLED(true),
    CAUGHT(true),
    LBW(true),
    RUN_OUT(true),
    STUMPED(true);

    private final boolean creditedBowler;

    WicketType(boolean creditedBowler) {
        this.creditedBowler = creditedBowler;
    }

    public boolean isCreditedToBowler(){
        return creditedBowler;
    }
}
