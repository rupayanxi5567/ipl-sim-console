package ipl.models;

import ipl.models.enums.Role;

import java.util.Objects;

public class Player implements Comparable<Player> {
    private final String id;
    private final String name;
    private final int age;
    private final String nationality;
    private final boolean overseas;
    private final Role role;
    private final long basePriceLakhs;
    private final BattingProfile batting;
    private final BowlingProfile bowling;
    private final int fielding;
    private final int overall;

    @Override
    public String toString() {
        return String.format("[%s] %s | %s | OVR %d | %s",
                id, name, role, overall, formatPrice(basePriceLakhs));
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getNationality() {
        return nationality;
    }

    public boolean isOverseas() {
        return overseas;
    }

    public Role getRole() {
        return role;
    }

    public long getBasePriceLakhs() {
        return basePriceLakhs;
    }

    public BattingProfile getBatting() {
        return batting;
    }

    public BowlingProfile getBowling() {
        return bowling;
    }

    public int getFielding() {
        return fielding;
    }

    public int getOverall() {
        return overall;
    }

    public Player(String id, String name, int age, String nationality, boolean overseas, Role role, long basePriceLakhs, BattingProfile batting, BowlingProfile bowling, int fielding, int overall) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.nationality = nationality;
        this.overseas = overseas;
        this.role = role;
        this.basePriceLakhs = basePriceLakhs;
        this.batting = batting;
        this.bowling = bowling;
        this.fielding = fielding;
        this.overall = overall;
    }

    public static String formatPrice(long lakhs) {
        if (lakhs >= 100) {
            double crores = (double) lakhs / 100.0;
            return String.format("₹%.2f Cr", crores);
        } else {
            return "₹" + lakhs + " L";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(id, player.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Player others) {
        int c = Integer.compare(others.overall, this.overall);
        if (c != 0) {
            return c;
        }
        return this.id.compareTo(others.id);
    }
}
