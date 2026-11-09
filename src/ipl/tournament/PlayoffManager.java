package ipl.tournament;

import ipl.models.Team;
import ipl.simulation.MatchSimulator;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PlayoffManager {
    private final Map<MatchStage, BracketNode> nodes = new EnumMap<>(MatchStage.class);
    private final Random random;
    private MatchStage current = MatchStage.QUALIFIER_1;

    public PlayoffManager(List<Team> topFour, Random random) {
        if (topFour.size() != 4) {
            throw new IllegalArgumentException("Playoffs need exactly 4 teams, got " + topFour.size());
        }
        this.random = random;

        BracketNode q1 = new BracketNode(MatchStage.QUALIFIER_1, topFour.get(0), topFour.get(1));
        BracketNode eliminator = new BracketNode(MatchStage.ELIMINATOR, topFour.get(2), topFour.get(3));
        BracketNode q2 = new BracketNode(MatchStage.QUALIFIER_2, new BracketNode.Feed(q1, true), new BracketNode.Feed(eliminator, false));
        BracketNode grandFinal = new BracketNode(MatchStage.FINAL, new BracketNode.Feed(q1, false), new BracketNode.Feed(q2, false));

        nodes.put(MatchStage.QUALIFIER_1, q1);
        nodes.put(MatchStage.ELIMINATOR, eliminator);
        nodes.put(MatchStage.QUALIFIER_2, q2);
        nodes.put(MatchStage.FINAL, grandFinal);
    }

    public boolean isFinished() {
        return current == null;
    }

    public MatchStage getCurrentStage() {
        return current;
    }

    public BracketNode playNext() {
        if (isFinished()) {
            throw new IllegalStateException("Playoffs are over");
        }
        BracketNode node = nodes.get(current);
        node.resolveTeams();
        node.setResult(MatchSimulator.playKnockOut(node.getTeamA(), node.getTeamB(), random));
        current = current.next();
        return node;
    }

    public void playAll() {
        while (!isFinished()) {
            playNext();
        }
    }

    public BracketNode getNode(MatchStage stage) {
        return nodes.get(stage);
    }

    public BracketNode getFinalNode() {
        return nodes.get(MatchStage.FINAL);
    }

    public Team getChampion() {
        if (!isFinished()) {
            return null;
        }
        return getFinalNode().getResult().getWinner();
    }
}