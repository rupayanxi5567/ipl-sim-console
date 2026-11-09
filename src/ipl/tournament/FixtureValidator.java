package ipl.tournament;

import ipl.models.Team;

import java.util.ArrayList;
import java.util.List;

public class FixtureValidator {
    public static List<String> validate(List<Team> teams, List<Fixture> fixtures) {
        List<String> problems = new ArrayList<>();
        int n = teams.size();

        if (fixtures.size() != n * (n - 1)) {
            problems.add("Expected " + (n * (n - 1)) + " fixtures, got " + fixtures.size());
        }

        int[][] games = new int[n][n];
        for (Fixture f : fixtures) {
            games[teams.indexOf(f.home())][teams.indexOf(f.away())]++;
        }
        for (int h = 0; h < n; h++) {
            for (int a = 0; a < n; a++) {
                int expected = (h == a) ? 0 : 1;
                if (games[h][a] != expected) {
                    problems.add("Pair home=" + teams.get(h).getShortName() + " away="
                            + teams.get(a).getShortName() + " appears " + games[h][a] + " times");
                }
            }
        }

        int rounds = 2 * (n - 1);
        int[][] playsInRound = new int[rounds + 1][n];
        for (Fixture f : fixtures) {
            playsInRound[f.round()][teams.indexOf(f.home())]++;
            playsInRound[f.round()][teams.indexOf(f.away())]++;
        }
        for (int r = 1; r <= rounds; r++) {
            for (int t = 0; t < n; t++) {
                if (playsInRound[r][t] != 1) {
                    problems.add("Round " + r + ": " + teams.get(t).getShortName()
                            + " plays " + playsInRound[r][t] + " times");
                }
            }
        }
        return problems;
    }

    public static int countBreaks(List<Team> teams, List<Fixture> fixtures) {
        int n = teams.size();
        int rounds = 2 * (n - 1);
        int[][] venue = new int[n][rounds + 1];
        for (Fixture f : fixtures) {
            venue[teams.indexOf(f.home())][f.round()] = 1;
            venue[teams.indexOf(f.away())][f.round()] = -1;
        }
        int breaks = 0;
        for (int t = 0; t < n; t++) {
            for (int r = 2; r <= rounds; r++) {
                if (venue[t][r] == venue[t][r - 1]) {
                    breaks++;
                }
            }
        }
        return breaks;
    }
}