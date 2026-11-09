package ipl.simulation;

import ipl.models.Player;

public class BowlerFigures {
    private final Player player;
    private int legalBalls;
    private int runs;
    private int wickets;

    public BowlerFigures(Player player){
        this.player=player;
    }

    public void addLegalBall(){
        legalBalls++;
    }

    public void addWicket(){
        wickets++;
    }

    public void addRuns(int r){
        runs+=r;
    }

    public String overs() {
        return (legalBalls / 6) + "." + (legalBalls % 6);
    }

    public double economy(){
        if(legalBalls==0){
            return 0.0;
        }
        return runs * 6.0 / legalBalls;
    }

    public Player getPlayer() {
        return player;
    }

    public int getLegalBalls() {
        return legalBalls;
    }

    public int getRuns() {
        return runs;
    }

    public int getWickets() {
        return wickets;
    }
}