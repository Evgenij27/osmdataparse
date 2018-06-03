package org.helio.document.relation;

import net.bytebuddy.matcher.CollectionElementMatcher;
import org.helio.calculation.GaussAreaCalculations;
import org.helio.document.node.Node;
import org.helio.document.way.Way;
import org.helio.document.way.WayBuilder;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Relation {
    public static final String ID = "id";
    public static final String VISIBLE = "visible";
    public static final String VERSION = "version";
    public static final String CHANGESET = "changeset";
    public static final String TIMESTAMP = "timestamp";
    public static final String USER = "user";
    public static final String UID = "uid";

    private final long id;
    private final boolean visible;
    private final int version;
    private final long changeset;
    private final String timestamp;
    private final String user;
    private final long uid;
    private final Map<String, String> tags;
    private final List<Member> members;

    public Relation(long id, boolean visible, int version, long changeset, String timestamp, String user,
                    long uid, Map<String, String> tags, List<Member> members) {
        this.id = id;
        this.visible = visible;
        this.version = version;
        this.changeset = changeset;
        this.timestamp = timestamp;
        this.user = user;
        this.uid = uid;
        this.tags = new HashMap<>(tags);
        this.members = new ArrayList<>(members);

    }

    private Stream<Way> filterByRole(String role) {
        return members.stream()
                .filter(member -> member.hasRole(role))
                .map(Member::getWay);
    }

    private Stream<Way> closedOuter() {
        return filterByRole("outer")
                .filter(Way::isClosed);
    }

    private Stream<Way> openOuter() {
        return filterByRole("outer")
                .filter(way -> !way.isClosed());
    }

    private Stream<Way> closedInner() {
        return filterByRole("inner")
                .filter(Way::isClosed);
    }

    private Stream<Way> openInner() {
        return filterByRole("inner")
                .filter(way -> !way.isClosed());
    }


    private List<Node> flatWaysToNodes(Stream<Way> ways) {
        return ways
                .map(Way::getNodes)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private List<Way> mergeNodesToWays(List<Node> nodeList) {
        Map<Integer, List<Node>> splittedWay = new HashMap<>();
        int head = 0;
        int groupId = 0;
        for (int i = 1; i < nodeList.size();) {
            if (nodeList.get(head).equals(nodeList.get(i))) {
                splittedWay.put(groupId++, nodeList.subList(head, i + 1));
                head = i + 1;
                i = i + 2;
            } else  {
                i++;
            }
        }
        List<Way> newWays = new ArrayList<>();
        WayBuilder wb = new WayBuilder();
        for (Map.Entry<Integer, List<Node>> entry : splittedWay.entrySet()) {
            for (Node n : entry.getValue()) {
                wb.addNode(n);
            }
            long id = entry.getValue().get(0).getId();
            wb.setId(id);
            newWays.add(wb.createWay());
        }
        return newWays;
    }

    @Override
    public String toString() {
        return "Relation{" +
                "id=" + id +
                ", visible=" + visible +
                ", version=" + version +
                ", changeset=" + changeset +
                ", timestamp='" + timestamp + '\'' +
                ", user='" + user + '\'' +
                ", uid='" + uid + '\'' +
                ", tags=" + tags +
                ", members=" + members +
                '}';
    }

    public long getId() {
        return id;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public List<Member> getMembers() {
        return members;
    }

    public String getName() {
        return tags.getOrDefault("name", "No Name");
    }


    public boolean checkTag(String key, String withValue) {
        return tags.containsKey(key) && tags.get(key).equals(withValue);
    }


    public Stream<Way> getOuterMembers() {
        List<Way> ways = closedOuter().collect(Collectors.toList());
        List<Way> mergedWay = mergeNodesToWays(flatWaysToNodes(openOuter()));
        ways.addAll(mergedWay);
        return ways.stream();
    }

    public Stream<Way> getInnerMembers() {
        List<Way> ways = closedInner().collect(Collectors.toList());
        List<Way> mergedWay = mergeNodesToWays(flatWaysToNodes(openInner()));
        ways.addAll(mergedWay);
        return ways.stream();
    }

    public Stream<Way> getMergedMembers() {
        List<Way> outer = getOuterMembers().collect(Collectors.toList());
        List<Way> inner = getInnerMembers().collect(Collectors.toList());
        outer.addAll(inner);
        return outer.stream();
    }

    public Way createComplexWay() {
        Set<Node> complexWay = members.stream()
                .map(Member::getWay)
                .map(Way::getNodes)
                .flatMap(List::stream)
                .collect(Collectors.toSet());
        WayBuilder wb = new WayBuilder();
        for (Node node : complexWay) {
            wb.addNode(node);
        }
        return wb.createWay();
    }

}
