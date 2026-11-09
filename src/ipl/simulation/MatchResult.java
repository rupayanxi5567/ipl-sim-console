package ipl.simulation;

public class MatchResult {
    private final Innings first;
    private final Innings second;
    private final String summary;

    public MatchResult(Innings first, Innings second, String summary) {
        this.first = first;
        this.second = second;
        this.summary = summary;
    }

    public Innings getFirst() {
        return first;
    }

    public Innings getSecond() {
        return second;
    }

    public String getSummary() {
        return summary;
    }
}