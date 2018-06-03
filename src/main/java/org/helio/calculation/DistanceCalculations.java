package org.helio.calculation;

import org.helio.document.node.Node;

import java.math.BigDecimal;
import java.util.List;

public class DistanceCalculations {
    // Earth mean radius in meters
    private static final double EARTH_RADIUS = 6_378_137d;

    private static double toRadians(double degrees) {
        return Math.toRadians(degrees);
    }

    private static double delta(double d1, double d2) {
        return d1 - d2;
    }

    private static double calculateDistance(Node src, Node dst) {
        double srcLat = toRadians(src.getLatitude().doubleValue());
        double dstLat = toRadians(dst.getLatitude().doubleValue());

        double srcLon = toRadians(src.getLongtitude().doubleValue());
        double dstLon = toRadians(dst.getLongtitude().doubleValue());

        return Math.acos(Math.sin(srcLat)*Math.sin(dstLat)+Math.cos(srcLat)*Math.cos(dstLat)*Math.cos(dstLon-srcLon))*EARTH_RADIUS;
    }

    private static Node[] toArray(List<Node> nodes) {
        Node[] array = new Node[nodes.size()];
        nodes.toArray(array);
        return array;
    }

    public static double calculateDistance(List<Node> nodes) {
        return calculateDistance(toArray(nodes));
    }

    public static double calculateDistance(Node[] nodes) {
        double distance = 0;
        for (int i = 0; i < nodes.length - 1; i++) {
            distance += calculateDistance(nodes[i], nodes[i+1]);
        }
        return distance;
    }


}
