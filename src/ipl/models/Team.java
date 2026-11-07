package ipl.models;

import ipl.models.enums.Role;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Team {
    private final String name;
    private final String shortName;
    private final long initialBudgetLakhs;
    private long currentBudgetLakhs;
    private final List<Player> squad;

    private static final int MIN_SQUAD_SIZE = 18;
    private static final int MAX_SQUAD_SIZE = 25;
    private static final int MAX_OVERSEAS_PLAYER = 8;

    public Team(String name, String shortName, long initialBudgetLakhs,long currentBudgetLakhs, List<Player> squad) {
        this.name = name;
        this.shortName = shortName;
        this.initialBudgetLakhs = initialBudgetLakhs;
        this.currentBudgetLakhs = currentBudgetLakhs;
        this.squad = new ArrayList<>(squad);
    }

    public Team(String name, String shortName, long initialBudgetLakhs, List<Player> squad) {
        this.name = name;
        this.shortName = shortName;
        this.initialBudgetLakhs = initialBudgetLakhs;
        this.currentBudgetLakhs = initialBudgetLakhs;
        this.squad = new ArrayList<>(squad);
    }

    public boolean canAdd(long price, Player p) {
        if (p == null || squad.contains(p)) {
            return false;
        }

        if (p.isOverseas() && overseasCount() >= MAX_OVERSEAS_PLAYER) {
            return false;
        }

        if (this.squad.size() >= this.MAX_SQUAD_SIZE) {
            return false;
        }

        if (price > currentBudgetLakhs) {
            return false;
        }

        int slotStillNeeded = Math.max(0, MIN_SQUAD_SIZE - 1 - squad.size());
        int reserve = slotStillNeeded * 20;

        if(currentBudgetLakhs-price < reserve){
            return false;
        }

        return true;
    }

    public int overseasCount() {
        int counter = 0;
        for (Player p : squad) {
            if (p.isOverseas()) {
                counter++;
            }
        }
        return counter;
    }

    public boolean canAfford(long price){
        return price <= currentBudgetLakhs;
    }

    public int countByRole(Role r){
        int counter=0;
        for(Player p:squad){
            if(p.getRole() == r){
                counter++;
            }
        }
        return counter;
    }

    public void addPlayer(Player p, long price){
        if(!canAdd(price,p)){
            throw new IllegalArgumentException("Cannot add player: Squad limit, overseas limit, or budget exceeded!");
        }
        squad.add(p);
        this.currentBudgetLakhs -= price;
    }

    public void addToSquadUnchecked(Player p) {
        squad.add(p);
    }

    public long totalSpent(){
        return initialBudgetLakhs - currentBudgetLakhs;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public long getInitialBudgetLakhs() {
        return initialBudgetLakhs;
    }

    public long getCurrentBudgetLakhs() {
        return currentBudgetLakhs;
    }

    public List<Player> getSquad() {
        return Collections.unmodifiableList(squad);
    }

    public int getMIN_SQUAD_SIZE() {
        return MIN_SQUAD_SIZE;
    }

    public int getMAX_SQUAD_SIZE() {
        return MAX_SQUAD_SIZE;
    }

    public int getMAX_OVERSEAS_PLAYER() {
        return MAX_OVERSEAS_PLAYER;
    }
}
