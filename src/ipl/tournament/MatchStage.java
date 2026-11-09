package ipl.tournament;

public enum MatchStage {
    QUALIFIER_1("Qualifier 1"),
    ELIMINATOR("Eliminator"),
    QUALIFIER_2("Qualifier 2"),
    FINAL("Final");

    private final String label;

    MatchStage(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public MatchStage next() {
        MatchStage[] all = values();
        int i = ordinal() + 1;
        return i < all.length ? all[i] : null;
    }
}