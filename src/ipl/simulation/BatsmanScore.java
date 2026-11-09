package ipl.simulation;

import ipl.models.Player;

public class BatsmanScore {
    private final Player player;
    private int runs;
    private int balls;
    private int fours;
    private int sixes;
    private boolean batted;
    private String dismissal;

    public BatsmanScore(Player player){
        this.player=player;
    }

    public void addBall(){
        balls++;
    }

    public void addRuns(int r){
        runs+=r;
        if(r==4){
            fours++;
        }else if(r==6){
            sixes++;
        }
    }

    public double strikeRate(){
        if(balls==0){
            return 0.0;
        }
        return (double) (runs * 100) / balls;
    }

    public Player getPlayer() {
        return player;
    }

    public int getRuns() {
        return runs;
    }

    public int getBalls() {
        return balls;
    }

    public int getFours() {
        return fours;
    }

    public int getSixes() {
        return sixes;
    }

    public boolean hasBatted() {
        return batted;
    }

    public void setBatted(boolean batted) {
        this.batted = batted;
    }

    public boolean isOut() {
        return dismissal != null;
    }

    public String getDismissal() {
        return dismissal;
    }

    public void setDismissal(String dismissal) {
        this.dismissal = dismissal;
    }
}
