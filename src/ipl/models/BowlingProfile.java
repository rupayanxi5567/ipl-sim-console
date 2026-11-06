package ipl.models;

import ipl.models.enums.BowlerType;

public class BowlingProfile {
    private final double economy;
    private final int wicketAbility;   //(1-100)
    private final int deathSkill;      //(1-100)
    private final int powerplaySkill;
    private final BowlerType type;

    public static BowlingProfile none() {
        return new BowlingProfile(0.0, 0, 0, 0, BowlerType.NONE);
    }

    @Override
    public String toString() {
        return "BowlingProfile{" +
                "economy=" + economy +
                ", wicketAbility=" + wicketAbility +
                ", deathSkill=" + deathSkill +
                ", powerplaySkill=" + powerplaySkill +
                ", type=" + type +
                '}';
    }

    public BowlingProfile(double economy, int wicketAbility, int deathSkill, int powerplaySkill, BowlerType type) {
        this.economy = economy;
        this.wicketAbility = wicketAbility;
        this.deathSkill = deathSkill;
        this.powerplaySkill = powerplaySkill;
        this.type = type;
    }

    public double getEconomy() {
        return economy;
    }

    public int getWicketAbility() {
        return wicketAbility;
    }

    public int getDeathSkill() {
        return deathSkill;
    }

    public int getPowerplaySkill() {
        return powerplaySkill;
    }

    public BowlerType getType() {
        return type;
    }
}
