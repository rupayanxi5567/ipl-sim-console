package ipl.tournament;

import ipl.models.Team;
import ipl.simulation.MatchResult;
import ipl.simulation.MatchSimulator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Season {
    private final List<Team> teams;
    private final List<Fixture> fixtures;
    private final PointsTable table;
    private final List<MatchResult> results = new ArrayList<>();
    private final Random random;
    private int nextMatch = 0;

    public Season(List<Team> teams, Random random) {
        this.teams = new ArrayList<>(teams);
        this.random = random;
        this.fixtures = FixtureGenerator.generate(teams);
        this.table = new PointsTable(teams);
    }

    public boolean hasNextMatch(){
        return nextMatch < fixtures.size();
    }

    public Fixture peekNextFixture(){
        return hasNextMatch() ? fixtures.get(nextMatch) : null;
    }

    public MatchResult playNextMatch(){
        if(!hasNextMatch()){
            throw new IllegalStateException("League stage is over");
        }
        Fixture f = fixtures.get(nextMatch);
        MatchResult result = MatchSimulator.play(f.home(), f.away(), random);
        results.add(result);
        table.record(result);
        nextMatch++;
        return result;
    }

    public void playAll(){
        while (hasNextMatch()){
            playNextMatch();
        }
    }

    public int matchesPlayed() {
        return nextMatch;
    }

    public int totalMatches() {
        return fixtures.size();
    }

    public List<Team> getTeams() {
        return Collections.unmodifiableList(teams);
    }

    public List<Fixture> getFixtures() {
        return Collections.unmodifiableList(fixtures);
    }

    public List<MatchResult> getResults() {
        return Collections.unmodifiableList(results);
    }

    public PointsTable getTable() {
        return table;
    }
}
