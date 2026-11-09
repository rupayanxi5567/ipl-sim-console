package ipl.simulation;

import ipl.models.Player;

public record Ball(int over, int ballInOver, Player batsman, Player bowler, Outcome outcome, WicketType wicketType) {
}