package org.helio.document.node;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class NodeBuilder {
    private long id;
    private BigDecimal latitude;
    private BigDecimal longtitude;
    private long uid;
    private String user;
    private long changeset;
    private int version;
    private boolean visible;
    private String timestamp;
    private Map<String, String> tags = new HashMap<>();

    public NodeBuilder setId(long id) {
        this.id = id;
        return this;
    }

    public NodeBuilder setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
        return this;
    }

    public NodeBuilder setLongtitude(BigDecimal longtitude) {
        this.longtitude = longtitude;
        return this;
    }

    public NodeBuilder setUid(long uid) {
        this.uid = uid;
        return this;
    }

    public NodeBuilder setUser(String user) {
        this.user = user;
        return this;
    }

    public NodeBuilder setChangeset(long changeset) {
        this.changeset = changeset;
        return this;
    }

    public NodeBuilder setVersion(int version) {
        this.version = version;
        return this;
    }

    public NodeBuilder setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    public NodeBuilder setTimestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public NodeBuilder addTag(String key, String value) {
        this.tags.put(key, value);
        return this;
    }

    public Node createNode() {
        return new Node(id, latitude, longtitude, uid, user, changeset, version, visible, timestamp, tags);
    }
}