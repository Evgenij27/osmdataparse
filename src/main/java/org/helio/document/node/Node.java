package org.helio.document.node;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;

public class Node {
    private static final BigDecimal RADIUS = BigDecimal.valueOf(6_378_137d);
    private static final BigDecimal F = BigDecimal.ONE.divide(BigDecimal.valueOf(298.257_224d), MathContext.DECIMAL64);
    private static final BigDecimal B = RADIUS.multiply(BigDecimal.ONE.subtract(F, MathContext.DECIMAL64), MathContext.DECIMAL64);
    private static final BigDecimal E2 = BigDecimal.ONE.subtract(
            (B.pow(2, MathContext.DECIMAL64).divide(RADIUS.pow(2, MathContext.DECIMAL64), MathContext.DECIMAL64))
    );

    public static final String ID = "id";
    public static final String LATITUDE = "lat";
    public static final String LONGTITUDE = "lon";
    public static final String UID = "uid";
    public static final String USER = "user";
    public static final String CHANGESET = "changeset";
    public static final String VERSION = "version";
    public static final String VISIBLE = "visible";
    public static final String TIMESTAMP = "timestamp";

    private final long id;
    private final BigDecimal latitude;
    private final BigDecimal longtitude;
    private final long uid;
    private final String user;
    private final long changeset;
    private final int version;
    private final boolean visible;
    private final String timestamp;
    private final Map<String, String> tags;
    private final BigDecimal x;
    private final BigDecimal y;

    public Node(long id, BigDecimal latitude, BigDecimal longtitude, long uid, String user, long changeset, int version,
                boolean visible, String timestamp, Map<String, String> tags) {
        this.id = id;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.uid = uid;
        this.user = user;
        this.changeset = changeset;
        this.version = version;
        this.visible = visible;
        this.timestamp = timestamp;
        this.tags = new HashMap<>(tags);
        this.x = calculateX();
        this.y = calculateY();
    }

    private BigDecimal calculateN() {
        double radLat = Math.toRadians(this.latitude.doubleValue());
        double sin2 = (1d-Math.cos(2d*radLat)) / 2d;
        double underRoot = 1d - E2.doubleValue() * sin2;
        double root = Math.sqrt(underRoot);
        return RADIUS.divide(BigDecimal.valueOf(root), MathContext.DECIMAL64);
    }

    private double toRadians(BigDecimal val) {
        return Math.toRadians(val.doubleValue());
    }

    private BigDecimal calculateX() {
        double r = Math.cos(toRadians(this.latitude)) * Math.cos(toRadians(this.longtitude));
        return calculateN().multiply(BigDecimal.valueOf(r));
    }

    private BigDecimal calculateY() {
        double r = Math.cos(toRadians(this.latitude)) * Math.sin(toRadians(this.longtitude));
        return calculateN().multiply(BigDecimal.valueOf(r));
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                //", latitude=" + latitude +
                //", longtitude=" + longtitude +
                //", x=" + x +
                //", y=" + y +
                '}';
    }

    public long getId() {
        return id;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public BigDecimal getLongtitude() {
        return longtitude;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return id == node.id &&
                Objects.equals(latitude, node.latitude) &&
                Objects.equals(longtitude, node.longtitude);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, latitude, longtitude);
    }

    public double getX() {
        return x.doubleValue();
    }

    public double getY() {
        return y.doubleValue();
    }
}
