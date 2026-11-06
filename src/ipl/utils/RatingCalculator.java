package ipl.utils;

import ipl.models.BattingProfile;
import ipl.models.BowlingProfile;
import ipl.models.Player;
import ipl.models.enums.BowlerType;
import ipl.models.enums.Role;

public class RatingCalculator {
    private static double normalize(double x, double min, double max) {
        x = Math.clamp(x, min, max);
        return (x - min) / (max - min) * 100;
    }

    public static double battingScore(BattingProfile batsman) {
        double strikeRate = normalize(batsman.getStrikeRate(), 100.0, 200.0);
        double average = normalize(batsman.getAverage(), 10.0, 50.0);
        int consistency = batsman.getConsistency();
        int aggression = batsman.getAggression();

        double battingScore = 0.45*strikeRate + 0.30*average + 0.15*consistency + 0.10*aggression;

        return battingScore;
    }

    public static double bowlingScore(BowlingProfile bowler) {
        if(bowler == null || bowler.getType() == BowlerType.NONE){
            return 0.0;
        }

        double economy = 100 - normalize(bowler.getEconomy(), 6, 12);
        int wicketAbility = bowler.getWicketAbility();
        int deathSkill = bowler.getDeathSkill();;
        int powerplaySkill = bowler.getPowerplaySkill();;

        double bowlingScore = 0.45*economy + 0.45*wicketAbility + 0.10*Math.max(deathSkill, powerplaySkill);

        return bowlingScore;
    }

    public static int calculateOverall(Player p){
        double battingValue = battingScore(p.getBatting());
        double bowlingValue = bowlingScore(p.getBowling());
        double fieldingValue = p.getFielding();

        double finalScore = 0.0;

        if(p.getRole() == Role.BATSMAN){
            finalScore = 0.85*battingValue + 0.15*fieldingValue;
        }else if(p.getRole() == Role.BOWLER){
            finalScore = 0.85*bowlingValue + 0.15*fieldingValue;
        }else if(p.getRole() == Role.BATTING_ALL_ROUNDER){
            finalScore = 0.50*battingValue + 0.35*bowlingValue + 0.15*fieldingValue;
        }else if(p.getRole() == Role.BOWLING_ALL_ROUNDER){
            finalScore = 0.35*battingValue + 0.50*bowlingValue + 0.15*fieldingValue;
        }else if(p.getRole() == Role.WICKET_KEEPER){
            finalScore = 0.75*battingValue + 0.25*fieldingValue;
        }

        return (int) Math.round(finalScore);
    }
}
