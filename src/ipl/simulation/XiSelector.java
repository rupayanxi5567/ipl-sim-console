package ipl.simulation;

import ipl.models.Player;
import ipl.models.Team;
import ipl.models.enums.BowlerType;
import ipl.models.enums.Role;
import ipl.utils.RatingCalculator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class XiSelector {
    private static final int XI_SIZE = 11;
    private static final int MAX_OVERSEAS_IN_XI = 4;
    private static final int MIN_BOWLERS = 5;

    private static boolean canBowl(Player p){
        return p.getBowling().getType() != BowlerType.NONE;
    }

    private static int countOverSeas(List<Player> xi){
        int counter=0;
        for(Player p:xi){
            if(p.isOverseas()){
                counter++;
            }
        }
        return counter;
    }

    public static int countBowler(List<Player> xi){
        int counter=0;
        for(Player p:xi){
            if(canBowl(p)){
                counter++;
            }
        }
        return counter;
    }

    private static boolean canTake(Player p,List<Player> xi){
        if(xi.contains(p)){
            return false;
        }
        if(xi.size()>=XI_SIZE){
            return false;
        }
        if(p.isOverseas() && countOverSeas(xi)>=MAX_OVERSEAS_IN_XI){
            return false;
        }

        return true;
    }

    public static List<Player> pickXi(Team team){
        List<Player> sorted = new ArrayList<>(team.getSquad());
        Collections.sort(sorted);

        List<Player> xi = new ArrayList<>();

        for(Player p:sorted){
            if(p.getRole() == Role.WICKET_KEEPER && canTake(p,xi)){
                xi.add(p);
                break;
            }
        } // taking keeper

        for(Player p:sorted){
            if(countBowler(xi) >= MIN_BOWLERS){
                break;
            }
            if(canBowl(p) && canTake(p,xi)){
                xi.add(p);
            }
        } // taking bowler

        for(Player p:sorted){
            if(xi.size() >= XI_SIZE){
                break;
            }
            if(canTake(p,xi)){
                xi.add(p);
            }
        } // taking rest players

        xi.sort(Comparator.comparingDouble((Player p) -> RatingCalculator.battingScore(p.getBatting())).reversed());
        return xi;
    }

    public static List<Player> bowlingAttack(List<Player> xi){
        List<Player> bowlingAttack = new ArrayList<>();

        for(Player p:xi){
            if(canBowl(p)){
                bowlingAttack.add(p);
            }
        }

        return bowlingAttack;
    }
}