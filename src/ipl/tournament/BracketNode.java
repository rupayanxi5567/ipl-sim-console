package ipl.tournament;

import ipl.models.Team;
import ipl.simulation.MatchResult;

public class BracketNode {
    public record Feed(BracketNode source, boolean takesLoser){
        public Team resolve(){
            MatchResult r = source.getResult();
            if(r == null){
                throw new IllegalStateException(source.getStage().getLabel() + " has not been played yet");
            }
            return takesLoser ? r.getLoser() : r.getWinner();
        }
    }

    private final MatchStage stage;
    private Team teamA;
    private Team teamB;
    private final Feed feedA;
    private final Feed feedB;
    private  MatchResult res;

    public BracketNode(MatchStage stage, Team teamA, Team teamB) {
        this.stage = stage;
        this.teamA = teamA;
        this.teamB = teamB;
        this.feedA = null;
        this.feedB = null;
    }

    public BracketNode(MatchStage stage, Feed feedA, Feed feedB) {
        this.stage = stage;
        this.feedA = feedA;
        this.feedB = feedB;
    }

    public boolean isLeaf(){
        return feedA == null;
    }

    public void resolveTeams(){
        if(!isLeaf()){
            teamA = feedA.resolve();
            teamB = feedB.resolve();
        }
    }

    public MatchStage getStage() {
        return stage;
    }

    public Team getTeamA() {
        return teamA;
    }

    public Team getTeamB() {
        return teamB;
    }

    public Feed getFeedA() {
        return feedA;
    }

    public Feed getFeedB() {
        return feedB;
    }

    public MatchResult getResult() {
        return res;
    }

    public void setResult(MatchResult result) {
        this.res = result;
    }
}
