package ipl.simulation;

import ipl.models.Player;
import ipl.models.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SuperOver {
    private static final int MAX_ROUNDS = 5;
    private static final int BALLS = 6;
    private static final int WICKETS = 2;

    public  static Team decide(Team batFirstTeam, List<Player> batFirstTeamXi, Team otherTeam, List<Player> otherTeamXi, Random random){

        for(int round=0; round<MAX_ROUNDS; round++){
            Innings first = oneOver(batFirstTeam, batFirstTeamXi, otherTeamXi, 0 , random);
            Innings second = oneOver(otherTeam, otherTeamXi, batFirstTeamXi, first.getRuns()+1 , random);

            if(first.getRuns() > second.getRuns()){
                return batFirstTeam;
            }
            if(first.getRuns() < second.getRuns()){
                return otherTeam;
            }
        }
        return random.nextDouble() <= 0.5 ? batFirstTeam : otherTeam;
    }

    private static Innings oneOver(Team team, List<Player> xi, List<Player> opposition, int target, Random random) {
        List<Player> batsmen = new ArrayList<>(xi.subList(0,3));
        List<Player> bowlingAttack = List.of(bestBowler(opposition));

        Innings innings = new Innings(team.getShortName(), batsmen, bowlingAttack, opposition, target, random, BALLS, WICKETS);
        innings.play();
        return innings;
    }

    private static Player bestBowler(List<Player> xi) {
        Player best = null;
        double bestPower = -1;
        for(Player p:XiSelector.bowlingAttack(xi)){
            double power = ProbabilityModel.bowlerPower(p,Phase.DEATH);
            if(power > bestPower){
                bestPower = power;
                best = p;
            }
        }
        return best;
    }
}
