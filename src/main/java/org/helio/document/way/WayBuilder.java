package org.helio.document.way;

import org.helio.document.node.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WayBuilder {
    private long id;
    private long uid;
    private String user;
    private long changeset;
    private int version;
    private boolean visible;
    private String timestamp;
    private Map<String, String> tags = new HashMap<>();
    private List<Node> nodes = new ArrayList<>();

    public WayBuilder setId(long id) {
        this.id = id;
        return this;
    }

    public WayBuilder setUid(long uid) {
        this.uid = uid;
        return this;
    }

    public WayBuilder setUser(String user) {
        this.user = user;
        return this;
    }

    public WayBuilder setChangeset(long changeset) {
        this.changeset = changeset;
        return this;
    }

    public WayBuilder setVersion(int version) {
        this.version = version;
        return this;
    }

    public WayBuilder setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    public WayBuilder setTimestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public WayBuilder addTag(String key, String value) {
        this.tags.put(key, value);
        return this;
    }

    public WayBuilder addNode(Node node) {
        this.nodes.add(node);
        return this;
    }

    public Way createWay() {
        return new Way(id, uid, user, changeset, version, visible, timestamp, tags, nodes);
    }
}