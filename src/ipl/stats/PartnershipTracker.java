package ipl.stats;

import ipl.simulation.Ball;
import ipl.simulation.Innings;
import ipl.simulation.Outcome;

import java.util.*;

public class PartnershipTracker {
    public static List<Partnership> compute(Innings innings){
        Deque<Partnership> stack = new ArrayDeque<>();
        int wicketNumber = 0;

        for (Ball ball : innings.getBalls()) {
            if (stack.isEmpty() || stack.peek().isBroken()) {
                stack.push(new Partnership(ball.batsman(), ball.nonStriker()));
            }

            Partnership current = stack.peek();
            current.add(ball.outcome().getRuns(), ball.outcome().isLegal());

            if (ball.outcome() == Outcome.WICKET) {
                wicketNumber++;
                current.markBroken(wicketNumber);
            }
        }

        List<Partnership> result = new ArrayList<>(stack);
        Collections.reverse(result);
        return result;
    }

    public static Partnership highest(List<Partnership> list) {
        Partnership best = null;
        for (Partnership p : list) {
            if (best == null || p.getRuns() > best.getRuns()) {
                best = p;
            }
        }
        return best;
    }
}
