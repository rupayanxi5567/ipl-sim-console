package ipl.models;

import ipl.models.enums.BattingHand;

public class BattingProfile {
    private final double average;
    private final double strikeRate;
    private final int aggression;
    private final int consistency;
    private final BattingHand hand;

    public BattingProfile(double average, double strikeRate, int aggression, int consistency, BattingHand hand) {
        this.average = average;
        this.strikeRate = strikeRate;
        this.aggression = aggression;
        this.consistency = consistency;
        this.hand = hand;
    }

    public int getAggression() {
        return aggression;
    }

    public double getAverage() {
        return average;
    }

    public double getStrikeRate() {
        return strikeRate;
    }

    public int getConsistency() {
        return consistency;
    }

    public BattingHand getHand() {
        return hand;
    }

    @Override
    public String toString() {
        return "BattingProfile{" +
                "average=" + average +
                ", strikeRate=" + strikeRate +
                ", aggression=" + aggression +
                ", consistency=" + consistency +
                ", hand=" + hand +
                '}';
    }
}
