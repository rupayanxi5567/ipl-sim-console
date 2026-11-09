package ipl.simulation;

import ipl.models.Team;

public class MatchResult {
    private final Team firstTeam;
    private final Team secondTeam;
    private final Innings first;
    private final Innings second;
    private final Team winner;
    private final String summary;

    public MatchResult(Team firstTeam, Team secondTeam, Innings first, Innings second, Team winner, String summary) {
        this.firstTeam = firstTeam;
        this.secondTeam = secondTeam;
        this.first = first;
        this.second = second;
        this.winner = winner;
        this.summary = summary;
    }

    public Team getFirstTeam() {
        return firstTeam;
    }

    public Team getSecondTeam() {
        return secondTeam;
    }

    public Innings getFirst() {
        return first;
    }

    public Innings getSecond() {
        return second;
    }

    public Team getWinner() {
        return winner;
    }

    public boolean isTie() {
        return winner == null;
    }

    public String getSummary() {
        return summary;
    }

    public Team getLoser(){
        if(winner == null){
            return null;
        }
        return (firstTeam==winner) ? secondTeam : firstTeam;
    }

    public boolean wasSuperOver(){
        return winner != null && first.getRuns() == second.getRuns();
    }
}