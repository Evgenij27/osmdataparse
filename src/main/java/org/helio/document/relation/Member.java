package org.helio.document.relation;

import org.helio.document.way.Way;

public class Member {
    public static final String REF = "ref";
    public static final String TYPE = "type";
    public static final String ROLE = "role";

    private final long ref;
    private final String type;
    private final String role;
    private final Way way;

    public Member(long ref, String type, String role, Way way) {
        this.ref = ref;
        this.type = type;
        this.role = role;
        this.way = way;
    }

    @Override
    public String toString() {
        return "Member{" +
                "ref=" + ref +
                ", type='" + type + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    public long getRef() {
        return ref;
    }

    public String getType() {
        return type;
    }

    public String getRole() {
        return role;
    }

    public Way getWay() {
        return way;
    }

    public boolean hasRole(String role) {
        return this.role.equals(role);
    }
}
