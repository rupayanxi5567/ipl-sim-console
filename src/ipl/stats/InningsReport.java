package ipl.stats;

import ipl.models.Player;
import ipl.simulation.Ball;
import ipl.simulation.Innings;
import ipl.simulation.Outcome;

import java.util.List;

public class InningsReport {

    public static void print(Innings inn) {
        OverStats stats = new OverStats(inn);
        int played = stats.getOversPlayed();

        System.out.println("--- " + inn.getBattingTeamName() + ": over by over ---");
        for (int over = 1; over <= played; over++) {
            int r = stats.runsInOver(over);
            System.out.printf("Over %2d: %2d  %s%n", over, r, "█".repeat(r));
        }

        System.out.println("--- Phase runs ---");
        if (played >= 6) {
            System.out.println("Powerplay (1-6):  " + stats.runsBetween(1, 6));
        }
        if (played >= 15) {
            System.out.println("Middle (7-15):    " + stats.runsBetween(7, 15));
        }
        if (played >= 16) {
            System.out.println("Death (16-" + played + "):    " + stats.runsBetween(16, played));
        }

        OverStats.Burst burst = stats.bestBurst(3);
        if (burst != null) {
            System.out.println("Best 3-over burst: overs " + burst.startOver() + "-" + burst.endOver()
                    + " (" + burst.runs() + " runs)");
        }

        System.out.println("--- Partnerships ---");
        List<Partnership> partnerships = PartnershipTracker.compute(inn);
        for (Partnership p : partnerships) {
            String ending = p.isBroken() ? "broken by wicket " + p.getEndedByWicket() : "not out";
            System.out.printf("%-20s & %-20s %3d (%d balls)  %s%n",
                    p.getBatterA().getName(), p.getBatterB().getName(),
                    p.getRuns(), p.getBalls(), ending);
        }
        Partnership top = PartnershipTracker.highest(partnerships);
        if (top != null) {
            System.out.println("Highest: " + top.getBatterA().getName() + " & "
                    + top.getBatterB().getName() + " = " + top.getRuns());
        }

        System.out.println("--- Fall of wickets ---");
        int score = 0;
        int wicketNumber = 0;
        for (Ball b : inn.getBalls()) {
            score += b.outcome().getRuns();
            if (b.outcome() == Outcome.WICKET) {
                wicketNumber++;
                Player out = b.batsman();
                System.out.println(wicketNumber + "-" + score + " (" + out.getName()
                        + ", " + b.over() + "." + b.ballInOver() + " ov)");
            }
        }
    }
}