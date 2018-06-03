package org.helio.calculation;

import org.helio.document.node.Node;
import org.helio.document.way.Way;

import java.math.BigDecimal;
import java.util.List;

public class GaussAreaCalculations {

    public static double calculateArea(Way way) {
        return calculateArea(way.getNodes());
    }

    public static double calculateArea(List<Node> nodeList) {
        Node[] nodes = toArray(nodeList);
        double partOne = 0;
        double tailOne = 0;
        double partTwo = 0;
        double tailTwo = 0;

        for (int i = 0; i < nodes.length - 1; i++) {
            partOne += (nodes[i].getX() * nodes[i+1].getY());
        }

        tailOne = (nodes[nodes.length - 1].getX() * nodes[0].getY());

        for (int i = 0; i < nodes.length - 1; i++) {
            partTwo += (nodes[i+1].getX() * nodes[i].getY());
        }

        tailTwo = (nodes[0].getX() * nodes[nodes.length - 1].getY());
        return Math.abs((partOne + tailOne - partTwo - tailTwo)/2d);
    }

    private static Node[] toArray(Way way) {
        List<Node> nodes = way.getNodes();
        return toArray(nodes);
    }

    private static Node[] toArray(List<Node> nodes) {
        Node[] array = new Node[nodes.size()];
        nodes.toArray(array);
        return array;
    }


}
