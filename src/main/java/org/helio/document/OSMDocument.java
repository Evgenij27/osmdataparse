package org.helio.document;

import org.helio.document.node.Node;
import org.helio.document.relation.Relation;
import org.helio.document.way.Way;

import java.util.ArrayList;
import java.util.List;

public class OSMDocument {
    public static final String BOUNDS = "bounds";
    public static final String NODE = "node";
    public static final String TAG = "tag";
    public static final String WAY = "way";
    public static final String ND = "nd";
    public static final String RELATION = "relation";
    public static final String MEMBER = "member";


    private final Bounds bounds;

    private final List<Node> nodes;
    private final List<Way> ways;
    private final List<Relation> relations;

    public OSMDocument(Bounds bounds, List <Node> nodes, List<Way> ways, List<Relation> relations) {
        this.bounds = bounds;
        this.nodes = new ArrayList<>(nodes);
        this.ways = new ArrayList<>(ways);
        this.relations = new ArrayList<>(relations);
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public List<Way> getWays() {
        return ways;
    }

    public List<Relation> getRelations() {
        return relations;
    }

    @Override
    public String toString() {
        return "OSMDocument{" +
                "bounds=" + bounds +
                ", nodes=" + nodes.size() +
                ", ways=" + ways.size() +
                ", relations=" + relations.size() +
                '}';
    }


}