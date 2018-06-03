package org.helio.document.way;

import org.helio.calculation.DistanceCalculations;
import org.helio.calculation.GaussAreaCalculations;
import org.helio.document.node.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Way {
    public static final String ID = "id";
    public static final String UID = "uid";
    public static final String USER = "user";
    public static final String CHANGESET = "changeset";
    public static final String VERSION = "version";
    public static final String VISIBLE = "visible";
    public static final String TIMESTAMP = "timestamp";

    private final long id;
    private final long uid;
    private final String user;
    private final long changeset;
    private final int version;
    private final boolean visible;
    private final String timestamp;
    private final Map<String, String> tags;
    private final List<Node> nodes;

    private double area;
    private double distance;

    public Way(long id, long uid, String user, long changeset, int version, boolean visible, String timestamp,
               Map<String, String> tags, List<Node> nodes) {
        this.id = id;
        this.uid = uid;
        this.user = user;
        this.changeset = changeset;
        this.version = version;
        this.visible = visible;
        this.timestamp = timestamp;
        this.tags = new HashMap<>(tags);
        this.nodes = new ArrayList<>(nodes);
    }

    @Override
    public String toString() {
        return "Way{" +
                "id=" + id +
                ", uid=" + uid +
                ", user='" + user + '\'' +
                ", changeset=" + changeset +
                ", version=" + version +
                ", visible=" + visible +
                ", timestamp='" + timestamp + '\'' +
                ", tags=" + tags +
                ", nodes=" + nodes +
                '}';
    }

    public long getId() {
        return id;
    }

    public long getUid() {
        return uid;
    }

    public String getUser() {
        return user;
    }

    public long getChangeset() {
        return changeset;
    }

    public int getVersion() {
        return version;
    }

    public boolean isVisible() {
        return visible;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public Node getFirst() {
        return nodes.get(0);
    }

    public Node getLast() {
        return nodes.get(nodes.size() - 1);
    }

    public boolean checkTag(String key, String withValue) {
        return tags.containsKey(key) && tags.get(key).equals(withValue);
    }

    public boolean isClosed() {
        return getFirst().equals(getLast());
    }

    public void calculateArea() {
        if (isClosed()) {
            area = GaussAreaCalculations.calculateArea(nodes);
        }
    }

    public double getArea() {
        return area;
    }

    public void calculateDistance() {
        if (!isClosed()) {
            distance = DistanceCalculations.calculateDistance(nodes);
        }
    }

    public double getDistance() {
        return distance;
    }

    public String getName() {
        return tags.getOrDefault("name", "No name");
    }

    public void addWay(Way way) {
        this.nodes.addAll(way.getNodes());
    }
}
