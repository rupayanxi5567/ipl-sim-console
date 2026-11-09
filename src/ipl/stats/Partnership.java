package ipl.stats;

import ipl.models.Player;

public class Partnership {
    private final Player batsmanA;
    private final Player batsmanB;
    private int runs;
    private int balls;
    private int endedByWicket;
    private boolean broken;

    public Partnership(Player batsmanA,Player batsmanB){
        this.batsmanA=batsmanA;
        this.batsmanB=batsmanB;
    }

    public void add(int runs, boolean legalBall){
        this.runs += runs;
        if(legalBall){
            balls++;
        }
    }

    public void markBroken(int wicketNumber) {
        this.broken = true;
        this.endedByWicket = wicketNumber;
    }

    public boolean isBroken() {
        return broken;
    }

    public Player getBatterA() {
        return batsmanA;
    }

    public Player getBatterB() {
        return batsmanB;
    }

    public int getRuns() {
        return runs;
    }

    public int getBalls() {
        return balls;
    }

    public int getEndedByWicket() {
        return endedByWicket;
    }
}
