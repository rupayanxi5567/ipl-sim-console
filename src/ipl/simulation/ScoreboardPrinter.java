package ipl.simulation;

public class ScoreboardPrinter {

    public static void print(Innings inn) {
        System.out.println("==================================================================");
        System.out.printf("%s  %d/%d  (%s overs)  extras: %d%n",
                inn.getBattingTeamName(), inn.getRuns(), inn.getWickets(),
                inn.oversText(), inn.getExtras());
        System.out.println("------------------------------------------------------------------");
        System.out.printf("%-22s %-34s %4s %4s %3s %3s %6s%n",
                "Batter", "Dismissal", "R", "B", "4s", "6s", "SR");
        for (BatsmanScore b : inn.getBatterScores()) {
            if (!b.hasBatted()) {
                continue;
            }
            String how = b.isOut() ? b.getDismissal() : "not out";
            System.out.printf("%-22s %-34s %4d %4d %3d %3d %6.1f%n",
                    b.getPlayer().getName(), how, b.getRuns(), b.getBalls(),
                    b.getFours(), b.getSixes(), b.strikeRate());
        }
        System.out.println("------------------------------------------------------------------");
        System.out.printf("%-24s %5s %4s %3s %6s%n", "Bowler", "O", "R", "W", "Econ");
        for (BowlerFigures f : inn.getBowlerFigures()) {
            System.out.printf("%-24s %5s %4d %3d %6.2f%n",
                    f.getPlayer().getName(), f.overs(), f.getRuns(), f.getWickets(), f.economy());
        }
    }
}