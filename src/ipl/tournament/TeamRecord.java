package ipl.tournament;

import ipl.models.Team;

public class TeamRecord {
    private static final int WIN_POINTS = 2;
    private static final int TIE_POINTS = 1;

    private final Team team;
    private int played;
    private int won;
    private int lost;
    private int tied;
    private int points;

    private int runScored;
    private int ballFaced;

    private int runConceded;
    private int ballBowled;

    public TeamRecord(Team team) {
        this.team = team;
    }

    public void addWin(){
        won++;
        points += WIN_POINTS;
        played++;
    }

    public void addLost(){
        lost++;
        played++;
    }

    public void addTie(){
        tied++;
        played++;
        points += TIE_POINTS;
    }

    public void addInnings(int runsFor, int ballsFor, int runsAgainst, int ballsAgainst) {
        runScored += runsFor;
        ballFaced += ballsFor;
        runConceded += runsAgainst;
        ballBowled += ballsAgainst;
    }

    public double netRunRate(){
        if(ballFaced==0 || ballBowled==0){
            return 0.0;
        }
        double forRate = runScored * 6.0 / ballFaced;
        double againstRate = runConceded * 6.0 / ballBowled;

        double netRunRate = forRate - againstRate;

        return netRunRate;
    }

    public Team getTeam() {
        return team;
    }

    public int getPlayed() {
        return played;
    }

    public int getWon() {
        return won;
    }

    public int getLost() {
        return lost;
    }

    public int getTied() {
        return tied;
    }

    public int getPoints() {
        return points;
    }

    public int getRunsScored() {
        return runScored;
    }

    public int getBallsFaced() {
        return ballFaced;
    }

    public int getRunsConceded() {
        return runConceded;
    }

    public int getBallsBowled() {
        return ballBowled;
    }
}
