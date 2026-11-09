package ipl.tournament;

import java.util.List;

public class PointsTablePrinter {

    public static void print(PointsTable table) {
        System.out.println("=====================================================================");
        System.out.printf("%-4s %-28s %3s %3s %3s %3s %4s %8s%n",
                "Pos", "Team", "P", "W", "L", "T", "Pts", "NRR");
        System.out.println("---------------------------------------------------------------------");
        List<TeamRecord> standings = table.standings();
        for (int i = 0; i < standings.size(); i++) {
            TeamRecord r = standings.get(i);
            String marker = (i < 4) ? "*" : " ";
            System.out.printf("%-4s %-28s %3d %3d %3d %3d %4d %+8.3f%n",
                    (i + 1) + marker, r.getTeam().getName(), r.getPlayed(), r.getWon(),
                    r.getLost(), r.getTied(), r.getPoints(), r.netRunRate());
        }
        System.out.println("(* = top 4, Eligible for playoff)");
    }
}