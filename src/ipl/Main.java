package ipl;

import ipl.models.Player;
import ipl.models.Team;
import ipl.persistence.PlayerRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Team> teams = PlayerRepository.loadTeams();

        List<Player> all = new ArrayList<>();
        for (Team t : teams) {
            all.addAll(t.getSquad());
        }
        System.out.println("Total players: " + all.size());

        Collections.sort(all); // Player.compareTo: overall বেশি আগে

        System.out.println("--- Top 10 ---");
        for (int i = 0; i < 10 && i < all.size(); i++) {
            System.out.println(all.get(i));
        }

        System.out.println("--- Bottom 3 ---");
        for (int i = all.size() - 3; i < all.size(); i++) {
            System.out.println(all.get(i));
        }

        System.out.println("--- Teams ---");
        for (Team t : teams) {
            System.out.println(t.getShortName()
                    + " squad=" + t.getSquad().size()
                    + " overseas=" + t.overseasCount());
        }
    }
}