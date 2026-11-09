package ipl.simulation;

import ipl.models.Player;

public record Ball(int over, int ballInOver, Player batsman, Player nonStriker, Player bowler, Outcome outcome, WicketType wicketType) {
}