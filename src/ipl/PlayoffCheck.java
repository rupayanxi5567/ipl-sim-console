package ipl;

import ipl.models.Team;
import ipl.persistence.PlayerRepository;
import ipl.simulation.MatchResult;
import ipl.tournament.BracketNode;
import ipl.tournament.MatchStage;
import ipl.tournament.PlayoffManager;
import ipl.tournament.Season;
import ipl.tournament.TeamRecord;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class PlayoffCheck {
    private static int failures = 0;

    private static void check(boolean ok, String message) {
        if (!ok) {
            failures++;
            if (failures <= 5) {
                System.out.println("FAIL: " + message);
            }
        }
    }

    private static Set<Team> teamsOf(BracketNode node) {
        return new HashSet<>(List.of(node.getTeamA(), node.getTeamB()));
    }

    public static void main(String[] args) {
        List<Team> teams = PlayerRepository.loadTeams();
        Random random = new Random(21);
        int runs = 3000;
        int superOvers = 0;
        int[] titlesBySeed = new int[4];

        for (int i = 0; i < runs; i++) {
            Season season = new Season(teams, random);
            season.playAll();

            List<TeamRecord> standings = season.getTable().standings();
            List<Team> topFour = new ArrayList<>();
            for (int k = 0; k < 4; k++) {
                topFour.add(standings.get(k).getTeam());
            }

            PlayoffManager playoffs = season.startPlayoffs();
            playoffs.playAll();

            BracketNode q1 = playoffs.getNode(MatchStage.QUALIFIER_1);
            BracketNode eliminator = playoffs.getNode(MatchStage.ELIMINATOR);
            BracketNode q2 = playoffs.getNode(MatchStage.QUALIFIER_2);
            BracketNode grandFinal = playoffs.getFinalNode();

            for (BracketNode node : List.of(q1, eliminator, q2, grandFinal)) {
                MatchResult result = node.getResult();
                check(result != null && result.getWinner() != null,
                        node.getStage().getLabel() + " has no winner");
                if (result != null && result.wasSuperOver()) {
                    superOvers++;
                }
            }
            check(playoffs.isFinished(), "playoffs not finished");

            check(teamsOf(q1).equals(new HashSet<>(topFour.subList(0, 2))), "Q1 teams wrong");
            check(teamsOf(eliminator).equals(new HashSet<>(topFour.subList(2, 4))), "Eliminator teams wrong");
            check(teamsOf(q2).equals(new HashSet<>(List.of(
                    q1.getResult().getLoser(), eliminator.getResult().getWinner()))), "Q2 teams wrong");
            check(teamsOf(grandFinal).equals(new HashSet<>(List.of(
                    q1.getResult().getWinner(), q2.getResult().getWinner()))), "Final teams wrong");

            Team eliminated = eliminator.getResult().getLoser();
            check(!teamsOf(q2).contains(eliminated) && !teamsOf(grandFinal).contains(eliminated),
                    "eliminated team came back");

            Team champion = playoffs.getChampion();
            check(champion == grandFinal.getResult().getWinner(), "champion != final winner");
            check(topFour.contains(champion), "champion not in top 4");
            titlesBySeed[topFour.indexOf(champion)]++;
        }

        System.out.println("failures = " + failures + ", super overs = " + superOvers
                + " in " + (runs * 4) + " playoff matches");
        for (int seed = 0; seed < 4; seed++) {
            System.out.printf("seed %d won %.1f%% of titles%n", seed + 1, titlesBySeed[seed] * 100.0 / runs);
        }
    }
}