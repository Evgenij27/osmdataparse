package org.helio.document.relation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RelationBuilder {
    private long id;
    private boolean visible;
    private int version;
    private long changeset;
    private String timestamp;
    private String user;
    private long uid;
    private Map<String, String> tags = new HashMap<>();
    private List<Member> members = new ArrayList<>();

    public RelationBuilder setId(long id) {
        this.id = id;
        return this;
    }

    public RelationBuilder setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    public RelationBuilder setVersion(int version) {
        this.version = version;
        return this;
    }

    public RelationBuilder setChangeset(long changeset) {
        this.changeset = changeset;
        return this;
    }

    public RelationBuilder setTimestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public RelationBuilder setUser(String user) {
        this.user = user;
        return this;
    }

    public RelationBuilder setUid(long uid) {
        this.uid = uid;
        return this;
    }

    public RelationBuilder addTag(String key, String value) {
        this.tags.put(key, value);
        return this;
    }

    public RelationBuilder addMember(Member member) {
        this.members.add(member);
        return this;
    }

    public Relation createRelation() {
        return new Relation(id, visible, version, changeset, timestamp, user, uid, tags, members);
    }
}