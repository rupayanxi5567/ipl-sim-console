package ipl.simulation;

import ipl.models.Player;
import ipl.models.enums.Role;

import java.util.*;

public class Innings {
    private static final int MAX_BALLS_PER_BOWLER = 24;
    private static final int DEFAULT_MAX_BALLS = 120;
    private static final int DEFAULT_MAX_WICKETS = 10;

    private final String battingTeamName;
    private final List<Player> battingOrder;
    private final List<Player> bowlingAttack;
    private final List<Player> fieldingXI;
    private final Player keeper;
    private final int target;
    private final Random random;
    private final int maxBalls;
    private final int maxWickets;

    private final List<Ball> balls = new ArrayList<>();
    private final Map<Player, BatsmanScore> batsmanScore = new LinkedHashMap<>();
    private final Map<Player, BowlerFigures> bowlerFigures = new LinkedHashMap<>();

    private int runs;
    private int wickets;
    private int legalBalls;
    private int extras;

    private int striker = 0;
    private int nonStriker = 1;
    private int nextBatsman = 2;

    private int over = 0;
    private int legalInOver = 0;
    private Player currentBowler;
    private Player lastBowler;

    public Innings(String battingTeamName, List<Player> battingOrder, List<Player> bowlingAttack, List<Player> fieldingXI, int target, Random random) {
        this(battingTeamName, battingOrder, bowlingAttack, fieldingXI, target, random,
                DEFAULT_MAX_BALLS, DEFAULT_MAX_WICKETS);
    }

    public Innings(String battingTeamName, List<Player> battingOrder, List<Player> bowlingAttack, List<Player> fieldingXI, int target, Random random, int maxBalls, int maxWickets) {
        this.battingTeamName = battingTeamName;
        this.battingOrder = battingOrder;
        this.bowlingAttack = bowlingAttack;
        this.fieldingXI = fieldingXI;
        this.keeper = findKeeper();
        this.target = target;
        this.random = random;
        this.maxBalls = maxBalls;
        this.maxWickets = maxWickets;

        for (Player p : battingOrder) {
            batsmanScore.put(p, new BatsmanScore(p));
        }
        batsmanScore.get(battingOrder.get(striker)).setBatted(true);
        batsmanScore.get(battingOrder.get(nonStriker)).setBatted(true);
    }

    public boolean isFinished() {
        if (legalBalls >= maxBalls) {
            return true;
        }
        if (wickets >= maxWickets) {
            return true;
        }
        return target > 0 && runs >= target;
    }

    public void play(){
        while (playNextBall()){}
    }

    public boolean playNextBall() {
        if(isFinished()){
            return false;
        }

        if(currentBowler == null){
            currentBowler = chooseBowler(over);
            legalInOver=0;
        }

        Phase phase = Phase.ofOver(over);
        BowlerFigures figures = bowlerFigures.computeIfAbsent(currentBowler, BowlerFigures::new);
        Player batsman = battingOrder.get(striker);
        Player partner = battingOrder.get(nonStriker);
        BatsmanScore score = batsmanScore.get(batsman);

        double[] cumulative = ProbabilityModel.cumulative(batsman, currentBowler, phase);
        Outcome outcome = ProbabilityModel.pick(cumulative,random.nextDouble());
        int r = outcome.getRuns();

        runs += r;
        figures.addRuns(r);

        if(outcome.isLegal()){
            legalBalls++;
            legalInOver++;
            figures.addLegalBall();
            score.addBall();
        }else{
            extras += r;
        }

        WicketType wicketType = null;
        if(outcome == Outcome.WICKET){
            wickets++;
            wicketType = rollWicketType(currentBowler);
            score.setDismissal(describeDismissal(wicketType,currentBowler));
            if(wicketType.isCreditedToBowler()){
                figures.addWicket();
            }
            if(wickets < maxWickets){
                striker=nextBatsman;
                nextBatsman++;
                batsmanScore.get(battingOrder.get(striker)).setBatted(true);
            }
        }else if(outcome.isLegal()) {
            score.addRuns(r);
            if(r%2==1){
                swapEnds();
            }
        }

        balls.add(new Ball(over, legalInOver, batsman, partner, currentBowler, outcome, wicketType));

        if(legalInOver == 6){
            endOver();
        }

        return true;
    }

    private Player findKeeper(){
        for(Player p:fieldingXI){
            if(p.getRole() == Role.WICKET_KEEPER){
                return p;
            }
        }
        return null;
    }

    private Player pickFielder(Player bowler){
        Player fielder;
        do{
            fielder = fieldingXI.get(random.nextInt(fieldingXI.size()));
        }while (fielder==bowler);
        return fielder;
    }

    private WicketType rollWicketType(Player bowler){
        WicketType type = WicketModel.pick(bowler.getBowling().getType(), random);
        if(type == WicketType.STUMPED && keeper==null){
            return WicketType.CAUGHT;
        }
        return type;
    }

    private String describeDismissal(WicketType type, Player bowler) {
        String b = bowler.getName();
        return switch (type) {
            case BOWLED -> "b " + b;
            case LBW -> "lbw b " + b;
            case CAUGHT -> "c " + pickFielder(bowler).getName() + " b " + b;
            case STUMPED -> "st " + keeper.getName() + " b " + b;
            case RUN_OUT -> "run out (" + pickFielder(bowler).getName() + ")";
        };
    }

    private void endOver(){
        swapEnds();
        lastBowler=currentBowler;
        currentBowler=null;
        over++;
    }

    private void swapEnds() {
        int tep=striker;
        striker=nonStriker;
        nonStriker=tep;
    }

    private Player chooseBowler(int over){
        Player best = bestBowler(over,true);

        if(best==null){
            best=bestBowler(over,false);
        }

        if (best == null) {
            throw new IllegalStateException(
                    "No available bowler for over " + (over + 1) + ". Check bowling attack and bowling limits."
            );
        }

        return best;
    }

    private Player bestBowler(int over, boolean avoidLast) {
        Phase phase = Phase.ofOver(over);
        Player best = null;
        double bestScore = -1;

        for(Player p:bowlingAttack){
            BowlerFigures f = bowlerFigures.get(p);
            int bowledBalls = (f==null) ? 0 : f.getLegalBalls();

            if(bowledBalls >= MAX_BALLS_PER_BOWLER){
                continue;
            }

            if(avoidLast && p==lastBowler){
                continue;
            }

            double score = ProbabilityModel.bowlerPower(p, phase) + random.nextDouble() * 10;

            if(score > bestScore){
                bestScore=score;
                best=p;
            }
        }
        return best;
    }

    public String getBattingTeamName() {
        return battingTeamName;
    }

    public int getRuns() {
        return runs;
    }

    public int getWickets() {
        return wickets;
    }

    public int getLegalBalls() {
        return legalBalls;
    }

    public int getExtras() {
        return extras;
    }

    public String oversText() {
        return (legalBalls / 6) + "." + (legalBalls % 6);
    }

    public Ball getLastBall() {
        return balls.getLast();
    }

    public List<Ball> getBalls() {
        return balls;
    }

    public Collection<BatsmanScore> getBatterScores() {
        return batsmanScore.values();
    }

    public Collection<BowlerFigures> getBowlerFigures() {
        return bowlerFigures.values();
    }

}
