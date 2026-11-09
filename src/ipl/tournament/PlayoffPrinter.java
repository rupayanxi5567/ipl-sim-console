package ipl.tournament;

import java.util.HashSet;
import java.util.Set;

public class PlayoffPrinter {

    public static void printBracket(PlayoffManager manager) {
        System.out.println("=====================================================================");
        System.out.println("PLAYOFF BRACKET");
        printNode(manager.getFinalNode(), "", "", new HashSet<>());
        if (manager.isFinished()) {
            System.out.println("---------------------------------------------------------------------");
            System.out.println("CHAMPION: " + manager.getChampion().getName());
        }
    }

    private static void printNode(BracketNode node, String indent, String prefix, Set<BracketNode> printed) {
        String label = node.getStage().getLabel();
        if (!printed.add(node)) {
            System.out.println(indent + prefix + label + " (previously shown)");
            return;
        }

        String teams = (node.getTeamA() == null)
                ? "TBD"
                : node.getTeamA().getShortName() + " vs " + node.getTeamB().getShortName();
        String outcome = (node.getResult() == null) ? "(not played)" : node.getResult().getSummary();
        System.out.println(indent + prefix + label + ": " + teams + " -> " + outcome);

        if (!node.isLeaf()) {
            printFeed(node.getFeedA(), indent + "    ", printed);
            printFeed(node.getFeedB(), indent + "    ", printed);
        }
    }

    private static void printFeed(BracketNode.Feed feed, String indent, Set<BracketNode> printed) {
        String prefix = feed.takesLoser() ? "loser of " : "winner of ";
        printNode(feed.source(), indent, prefix, printed);
    }
}