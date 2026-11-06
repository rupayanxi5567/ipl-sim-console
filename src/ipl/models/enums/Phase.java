package ipl.models.enums;

public enum Phase {
    POWERPLAY(1, 6),
    MIDDLE(7, 15),
    DEATH(16, 20);

    private final int startOver;
    private final int endOver;

    Phase(int startOver, int endOver) {
        this.startOver = startOver;
        this.endOver = endOver;
    }

    public static Phase getPhaseFromOvers(int over) {
        for(Phase p:values()){
            if(p.startOver <= over && over <= p.endOver){
                return p;
            }
        }
        return DEATH;
    }

}
