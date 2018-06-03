package org.helio.document.relation;

import org.helio.document.way.Way;

public class MemberBuilder {
    private long ref;
    private String type;
    private String role;
    private Way way;

    public MemberBuilder setRef(long ref) {
        this.ref = ref;
        return this;
    }

    public MemberBuilder setType(String type) {
        this.type = type;
        return this;
    }

    public MemberBuilder setRole(String role) {
        this.role = role;
        return this;
    }

    public MemberBuilder setWay(Way way) {
        this.way = way;
        return this;
    }

    public Member createMember() {
        return new Member(ref, type, role, way);
    }
}