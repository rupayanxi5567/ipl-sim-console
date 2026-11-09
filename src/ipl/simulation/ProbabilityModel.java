package ipl.simulation;

import ipl.models.BowlingProfile;
import ipl.models.Player;
import ipl.utils.RatingCalculator;

public class ProbabilityModel {
    private static final double OFFSET = 40;
    private static final double CALIBRATION = 1.35;   // inc. this to get more avg runs
    private static final double DOT_SENSITIVITY = 0.3;
    private static final double FOUR_SENSITIVITY = 0.8;
    private static final double SIX_SENSITIVITY = 1.0;
    private static final double WICKET_SENSITIVITY = 0.7;

    private static final double[] BASE = {0.35, 0.30, 0.08, 0.01, 0.11, 0.06, 0.05, 0.03, 0.01};

    public static double bowlerPower(Player bowler,Phase phase){
        BowlingProfile bowlingProfile = bowler.getBowling();
        double base = RatingCalculator.bowlingScore(bowlingProfile);
        double skill=0.0;
        if(phase==Phase.POWERPLAY){
            skill = bowlingProfile.getPowerplaySkill();
        }else if(phase==Phase.DEATH){
            skill = bowlingProfile.getDeathSkill();
        }else{
            skill = bowlingProfile.getWicketAbility();
        }
        return 0.7*base + 0.3*skill;
    }

    private static void scale(double[] p, Outcome o, double factor) {
        p[o.ordinal()] *= factor;
    }

    public static double[] cumulative(Player batsman, Player bowler, Phase phase){
        double[] p = BASE.clone();

        double batPower = RatingCalculator.battingScore(batsman.getBatting());
        double bowlPower = bowlerPower(bowler,phase);
        double ratio = CALIBRATION * (batPower + OFFSET) / (bowlPower + OFFSET);
        ratio = Math.clamp(ratio, 0.5,2.0);

        scale(p, Outcome.DOT, Math.pow(ratio,-DOT_SENSITIVITY));
        scale(p, Outcome.FOUR, Math.pow(ratio, FOUR_SENSITIVITY));
        scale(p, Outcome.SIX, Math.pow(ratio, SIX_SENSITIVITY));
        scale(p, Outcome.WICKET, Math.pow(ratio, -WICKET_SENSITIVITY));

        if(phase == Phase.POWERPLAY){
            scale(p,Outcome.FOUR,1.20);
            scale(p,Outcome.SIX,1.10);
            scale(p,Outcome.DOT,0.85);
            scale(p,Outcome.WICKET,0.90);
        }else if(phase == Phase.MIDDLE){
            scale(p,Outcome.ONE,1.10);
            scale(p,Outcome.TWO,1.10);
            scale(p,Outcome.FOUR,0.80);
            scale(p,Outcome.SIX,0.75);
        }else{
            scale(p,Outcome.FOUR,1.20);
            scale(p,Outcome.SIX,1.65);
            scale(p,Outcome.WICKET,1.30);
            scale(p,Outcome.DOT,0.75);
        }
        double sum=0;
        for (double v:p){
            sum+=v;
        }
        double[] cum = new double[p.length];
        double running = 0;
        for (int i = 0; i < p.length; i++) {
            running += p[i] / sum;
            cum[i] = running;
        }
        cum[cum.length-1] = 1.0;
        return cum;
    }

    public static int pickIndex(double[] cumulative, double r) {
        int lo = 0;
        int hi = cumulative.length - 1;
        while (lo < hi) {
            int mid = (lo + hi) / 2;
            if (cumulative[mid] > r) {
                hi = mid;
            } else {
                lo = mid + 1;
            }
        }
        return lo;
    }

    public static Outcome pick(double[] cumulative, double r) {
        return Outcome.values()[pickIndex(cumulative, r)];
    }
}
