package ipl.simulation;

public enum Outcome {
    DOT(0,true),
    ONE(1,true),
    TWO(2, true),
    THREE(3, true),
    FOUR(4, true),
    SIX(6, true),
    WICKET(0, true),
    WIDE(1, false),
    NO_BALL(1, false);

    private final int runs;
    private final boolean legal;

    Outcome(int runs, boolean legal) {
        this.runs = runs;
        this.legal = legal;
    }


    public int getRuns() {
        return runs;
    }

    public boolean isLegal() {
        return legal;
    }
}
