package ipl.tournament;

import ipl.models.Team;

import java.util.ArrayList;
import java.util.List;

public class FixtureGenerator {
    public static List<Fixture> generate(List<Team> teams){
        int n = teams.size();
        if (n < 2 || n % 2 != 0) {
            throw new IllegalArgumentException("Team count must be even, got " + n);
        }

        List<Team> rotation = new ArrayList<>(teams);
        List<Fixture> firstLeg = new ArrayList<>();

        for (int round = 0; round < n - 1; round++) {
            for (int i = 0; i < n / 2; i++) {
                Team a = rotation.get(i);
                Team b = rotation.get(n - 1 - i);

                boolean aIsHome = (i == 0) ? (round % 2 == 0) : (i % 2 == 0);
                if (aIsHome) {
                    firstLeg.add(new Fixture(round + 1, a, b));
                } else {
                    firstLeg.add(new Fixture(round + 1, b, a));
                }
            }
            Team last = rotation.remove(n - 1);
            rotation.add(1, last);
        }

        List<Fixture> all = new ArrayList<>(firstLeg);
        for (Fixture f : firstLeg) {
            all.add(new Fixture(f.round() + (n - 1), f.away(), f.home()));
        }
        return all;
    }
}
