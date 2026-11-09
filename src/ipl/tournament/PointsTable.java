package ipl.tournament;

import ipl.models.Team;
import ipl.simulation.Innings;
import ipl.simulation.MatchResult;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PointsTable {
    private static final int FULL_QUOTA_BALLS = 120;

    private final List<Team> teams;
    private final Map<Team, TeamRecord> records = new LinkedHashMap<>();
    private final int [][] headToHeadWins;

    public PointsTable(List<Team> teams){
        this.teams = new ArrayList<>(teams);
        for(Team t: teams){
            records.put(t,new TeamRecord(t));
        }
        headToHeadWins = new int[teams.size()][teams.size()];
    }

    private static int effectiveBalls(Innings inn){
        return inn.getWickets() >= 10 ? FULL_QUOTA_BALLS : inn.getLegalBalls();
    }

    public void record(MatchResult result){
        TeamRecord firstRecord = records.get(result.getFirstTeam());
        TeamRecord secondRecord = records.get(result.getSecondTeam());

        Innings i1 = result.getFirst();
        Innings i2 = result.getSecond();

        int balls1 = effectiveBalls(i1);
        int balls2 = effectiveBalls(i2);

        firstRecord.addInnings(i1.getRuns(), balls1, i2.getRuns(), balls2);
        secondRecord.addInnings(i2.getRuns(), balls2, i1.getRuns(), balls1);

        if(result.isTie()){
            firstRecord.addTie();
            secondRecord.addTie();
            return;
        }

        Team winner = result.getWinner();
        Team loser = (winner == result.getFirstTeam()) ? result.getSecondTeam() : result.getFirstTeam();

        records.get(winner).addWin();
        records.get(loser).addLost();

        headToHeadWins[teams.indexOf(winner)][teams.indexOf(loser)]++;
    }

    public int headToHead(Team x, Team y){
        return headToHeadWins[teams.indexOf(x)][teams.indexOf(y)]++;
    }

    public List<TeamRecord> standings() {
        List<TeamRecord> list = new ArrayList<>(records.values());
        list.sort(this::compareRecords);
        return list;
    }

    private int compareRecords(TeamRecord x, TeamRecord y) {
        int c = Integer.compare(y.getPoints(), x.getPoints());
        if (c != 0) {
            return c;
        }
        c = Double.compare(y.netRunRate(), x.netRunRate());
        if (c != 0) {
            return c;
        }
        c = Integer.compare(y.getWon(), x.getWon());
        if (c != 0) {
            return c;
        }
        return Integer.compare(headToHead(y.getTeam(), x.getTeam()), headToHead(x.getTeam(), y.getTeam()));
    }

    public TeamRecord getRecord(Team team) {
        return records.get(team);
    }

    public List<TeamRecord> getAllRecords() {
        return new ArrayList<>(records.values());
    }
}
