package ipl.simulation;

import ipl.models.enums.BowlerType;

import java.util.Random;

public class WicketModel {
    private static final double[] PACE = {0.21, 0.62, 0.10, 0.07, 0.00};
    private static final double[] SPIN = {0.20, 0.58, 0.09, 0.06, 0.07};

    private static final double[] PACE_CUM = buildCumulative(PACE);
    private static final double[] SPIN_CUM = buildCumulative(SPIN);


    private static double[] buildCumulative(double[] weights) {
        double []cum = new double[weights.length];
        double running = 0;
        int lastNonZero = 0;
        for(int i=0;i< weights.length;i++){
            running += weights[i];
            cum[i] = running;
            if(weights[i]>0){
                lastNonZero=i;
            }
        }
        for (int i = lastNonZero; i < weights.length; i++) {
            cum[i] = 1.0;
        }
        return cum;
    }

    public static WicketType pick(BowlerType bowlerType, Random random){
        double[] cum = (BowlerType.PACE == bowlerType) ? PACE_CUM : SPIN_CUM;
        int index = ProbabilityModel.pickIndex(cum, random.nextDouble());
        return WicketType.values()[index];
    }
}
