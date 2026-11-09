package ipl;

import ipl.models.Player;
import ipl.models.Team;
import ipl.persistence.PlayerRepository;
import ipl.simulation.XiSelector;

import java.util.List;

public class TeamStrengthDemo {
    public static void main(String[] args) {
        List<Team> teams = PlayerRepository.loadTeams();
        for (Team t : teams) {
            List<Player> xi = XiSelector.pickXi(t);
            double sum = 0;
            int top = 0;
            for (Player p : xi) {
                sum += p.getOverall();
                if (p.getOverall() >= 75) {
                    top++;
                }
            }
            System.out.printf("%-28s avg XI overall = %.1f, players >= 75: %d%n",
                    t.getName(), sum / xi.size(), top);
        }
    }
}